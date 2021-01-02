package edu.nuce.cinema.ui.anime

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.hideKeyboard
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.data.models.request.HistoryParams
import edu.nuce.cinema.databinding.FragmentAnimeBinding
import edu.nuce.cinema.ui.anime.AnimeFragmentDirections.Companion.actionAnimeToCommentDetailFragment
import edu.nuce.cinema.ui.anime.AnimeFragmentDirections.Companion.actionAnimeToLogin
import edu.nuce.cinema.ui.anime.dialog.SaveDialog
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.adapter.CommentAdapter
import edu.nuce.cinema.ui.common.constants.TypeSeries
import edu.nuce.cinema.ui.common.events.CommentEvent
import edu.nuce.cinema.ui.detail.DetailFragmentDirections
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToLogin
import edu.nuce.cinema.ui.detail.DetailIntent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AnimeFragment : BaseFragment(R.layout.fragment_anime), MviView<AnimeIntent, AnimeViewState>,
    YouTubePlayerFullScreenListener, CommentEvent, AnimeEvents {

    private lateinit var adapter: CommentAdapter
    private val args by navArgs<AnimeFragmentArgs>()
    private val _binding by viewBinding<FragmentAnimeBinding>()
    lateinit var saveDialog: SaveDialog

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager
    private val _getCommentItentPublisher =
        PublishSubject.create<AnimeIntent.GetCommentIntent>()
    private val _getSeriesItentPublisher =
        PublishSubject.create<AnimeIntent.GetSeriesIntent>()
    private val _updateViewItentPublisher =
        PublishSubject.create<AnimeIntent.UpdateAnimeIntent>()
    private val _commentItentPublisher =
        PublishSubject.create<AnimeIntent.CommentIntent>()
    private val _getArchiveItentPublisher =
        PublishSubject.create<AnimeIntent.GetArchiveIntent>()
    private val _getLikesItentPublisher =
        PublishSubject.create<AnimeIntent.GetLikeMeIntent>()
    private val _likesItentPublisher =
        PublishSubject.create<AnimeIntent.LikeIntent>()
    private val _viewModel: AnimeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        if (rxOAuthManager.isExistsCredentials)
            _getLikesItentPublisher.onNext(AnimeIntent.GetLikeMeIntent)
        _getSeriesItentPublisher.onNext(AnimeIntent.GetSeriesIntent(args.anime.id))
        _getCommentItentPublisher.onNext(AnimeIntent.GetCommentIntent(args.anime.id))
        _getArchiveItentPublisher.onNext(AnimeIntent.GetArchiveIntent(args.anime.id))
        _updateViewItentPublisher.onNext(
            AnimeIntent.UpdateAnimeIntent(
                args.anime.id,
                HistoryParams(args.anime.episode, seriesId = args.anime.id, TypeSeries.ANIME)
            )
        )
    }

    private fun playAnime(key: String?) {
        _binding.youtubePlayerView.apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                    youTubePlayer.loadVideo(key ?: "uuckCNO9jgM", 0F)
                }
            })
        }
    }

    override fun onYouTubePlayerEnterFullScreen() {
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onYouTubePlayerExitFullScreen() {
        findNavController().navigateUp()
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<AnimeIntent> {
        return Observable.merge(
            _getCommentItentPublisher,
            _getSeriesItentPublisher,
            _updateViewItentPublisher,
            _commentItentPublisher
        ).mergeWith(
            _getArchiveItentPublisher
        ).mergeWith(
            _getLikesItentPublisher
        ).mergeWith(
            _likesItentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: AnimeViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapter = CommentAdapter(this, requestManager, state.likeMe,false)
        adapter.submitList(state.comments)
        playAnime(args.anime.video)
        _binding.apply {
            btnSend.setOnClickListener {
                if (etComment.text != null) {
                    commentSeries(
                        CommentParams(
                            0,
                            etComment.text.toString(),
                            state.series!!.id,
                            TypeSeries.ANIME
                        )
                    )
                    adapter.submitList(state.comments)
                    etComment.text.clear()
                    hideKeyboard()
                }
            }
            rvComment.adapter = adapter
            tvSave.setOnClickListener {
                saveDialog =
                    SaveDialog(state.archives, args.anime.episode,TypeSeries.ANIME, state.series, this@AnimeFragment)
                if (rxOAuthManager.isExistsCredentials)
                    this@AnimeFragment.fragmentManager?.let { it ->
                        saveDialog.show(
                            it,
                            "name"
                        )
                    }
                else
                    findNavController().navigate(actionAnimeToLogin())
            }
        }
    }

    fun commentSeries(commentParams: CommentParams) {
        if (rxOAuthManager.isExistsCredentials) {
            _commentItentPublisher.onNext(
                AnimeIntent.CommentIntent(
                    commentParams,
                    args.anime.id.toInt()
                )
            )
            toast("Comment success")
        } else
            findNavController().navigate(actionAnimeToLogin())
    }

    override fun onLike(comment: Comment) {
        if (rxOAuthManager.isExistsCredentials) {
            _likesItentPublisher.onNext(AnimeIntent.LikeIntent(comment.id))
        } else
            findNavController().navigate(actionDetailToLogin())
    }

    override fun onParentComment(comment: Comment) {
        findNavController().navigate(actionAnimeToCommentDetailFragment(comment))
    }

    override fun onReport(comment: Comment) {
        TODO("Not yet implemented")
    }

    override fun onSubmit() {
        _getArchiveItentPublisher.onNext(AnimeIntent.GetArchiveIntent(args.anime.id))
    }
}
