package edu.nuce.cinema.ui.comment_manga


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.hideKeyboard
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.databinding.FragmentCommentMangaBinding
import edu.nuce.cinema.ui.anime.AnimeIntent
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.comment_manga.CommentMangaFragmentDirections.Companion.actionCommentMangaToCommentDetail
import edu.nuce.cinema.ui.comment_manga.CommentMangaFragmentDirections.Companion.actionCommentMangaToLogin
import edu.nuce.cinema.ui.comment_manga.CommentMangaFragmentDirections.Companion.actionCommentMangaToReport
import edu.nuce.cinema.ui.common.adapter.CommentAdapter
import edu.nuce.cinema.ui.common.constants.TypeSeries
import edu.nuce.cinema.ui.common.events.CommentEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CommentMangaFragment : BaseFragment(R.layout.fragment_comment_manga),
    MviView<CommentMangaIntent, CommentMangaViewState>, CommentEvent {

    private lateinit var adapter: CommentAdapter

    private val args by navArgs<CommentMangaFragmentArgs>()
    private val _binding by viewBinding<FragmentCommentMangaBinding>()

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager

    private val _getCommentItentPublisher =
        PublishSubject.create<CommentMangaIntent.GetCommentIntent>()
    private val _commentItentPublisher =
        PublishSubject.create<CommentMangaIntent.CommentIntent>()
    private val _viewModel: CommentMangaViewModel by viewModels()
    private val _getSeriesItentPublisher =
        PublishSubject.create<CommentMangaIntent.GetSeriesIntent>()
    private val _getLikesItentPublisher =
        PublishSubject.create<CommentMangaIntent.GetLikeMeIntent>()
    private val _likesItentPublisher =
        PublishSubject.create<CommentMangaIntent.LikeIntent>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        _getCommentItentPublisher.onNext(CommentMangaIntent.GetCommentIntent(args.manga.id))
        _getSeriesItentPublisher.onNext(CommentMangaIntent.GetSeriesIntent(args.manga.id))
        _getLikesItentPublisher.onNext(CommentMangaIntent.GetLikeMeIntent)
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<CommentMangaIntent> {
        return Observable.merge(
            _getCommentItentPublisher,
            _commentItentPublisher,
            _getSeriesItentPublisher,
            _getLikesItentPublisher
        ).mergeWith(
            _likesItentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: CommentMangaViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapter = CommentAdapter(this, requestManager, state.likeMe,false)
        adapter.submitList(state.comments)
        _binding.apply {
            include.apply {
                imgAction.setImageDrawable(resources.getDrawable(R.drawable.ic_previous_black))
                imgAction.setOnClickListener { onBackPressed() }
                title.text = resources.getString(R.string.comments)
            }
            rvComment.adapter = adapter
            btnSend.setOnClickListener {
                if (etComment.text != null) {
                    commentSeries(
                        CommentParams(
                            0, etComment.text.toString(),
                            state.series!!.id, TypeSeries.MANGA
                        )
                    )
                    adapter.submitList(state.comments)
                    etComment.text.clear()
                    hideKeyboard()
                }
            }
        }
    }

    fun commentSeries(commentParams: CommentParams) {
        if (rxOAuthManager.isExistsCredentials) {
            _commentItentPublisher.onNext(
                CommentMangaIntent.CommentIntent(
                    commentParams,
                    args.manga.id
                )
            )
            toast("Comment success")
        } else
            findNavController().navigate(actionCommentMangaToLogin())
    }

    override fun onLike(comment: Comment) {
        if (rxOAuthManager.isExistsCredentials)
            _likesItentPublisher.onNext(CommentMangaIntent.LikeIntent(comment.id))
        else
            findNavController().navigate(actionCommentMangaToLogin())
    }

    override fun onParentComment(comment: Comment) {
        findNavController().navigate(
            actionCommentMangaToCommentDetail(
                comment
            )
        )
    }

    override fun onReport(comment: Comment) {
        findNavController().navigate(actionCommentMangaToReport(comment.id))
    }
}
