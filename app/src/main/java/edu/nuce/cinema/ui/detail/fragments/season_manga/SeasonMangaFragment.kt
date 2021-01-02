package edu.nuce.cinema.ui.detail.fragments.season_manga

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.arg
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Season
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.FragmentSeasonMangaBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.adapter.CommentAdapter
import edu.nuce.cinema.ui.common.events.CommentEvent
import edu.nuce.cinema.ui.detail.DetailFragmentDirections
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToChapter
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToCommentDetail
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToLogin
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToReport
import edu.nuce.cinema.ui.detail.adapter.SeasonAdapter
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeIntent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@AndroidEntryPoint
class SeasonMangaFragment : BaseFragment(R.layout.fragment_season_manga), CommentEvent,
    MviView<SeasonMangaIntent, SeasonMangaViewState>, SeasonAdapter.SeasonOnClick {

    private lateinit var seasonAdapter: SeasonAdapter
    private lateinit var commentAdapter: CommentAdapter
    private val _binding by viewBinding<FragmentSeasonMangaBinding>()
    private val series by arg<Series?>(SeasonMangaFragment::class.java.simpleName)

    private val _getSeasonItentPublisher =
        PublishSubject.create<SeasonMangaIntent.GetSeasonsIntent>()
    private val _getTopCommentItentPublisher =
        PublishSubject.create<SeasonMangaIntent.GetTopCommentIntent>()
    private val _getLikesItentPublisher =
        PublishSubject.create<SeasonMangaIntent.GetLikeMeIntent>()
    private val _likesItentPublisher =
        PublishSubject.create<SeasonMangaIntent.LikeIntent>()
    private val _viewModel: SeasonMangaViewModel by viewModels()

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        if (rxOAuthManager.isExistsCredentials)
            _getLikesItentPublisher.onNext(SeasonMangaIntent.GetLikeMeIntent)
        series?.let {
            _getSeasonItentPublisher.onNext(SeasonMangaIntent.GetSeasonsIntent(it.id))
            _getTopCommentItentPublisher.onNext(SeasonMangaIntent.GetTopCommentIntent(it.id))
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<SeasonMangaIntent> {
        return Observable.merge(
            _getSeasonItentPublisher,
            _getTopCommentItentPublisher,
            _getLikesItentPublisher,
            _likesItentPublisher
        )
    }

    override fun render(state: SeasonMangaViewState) {
        if (state.seasons.size != 0) {
            seasonAdapter = SeasonAdapter(this)
            seasonAdapter.submitList(state.seasons)
            commentAdapter = CommentAdapter(this, requestManager,state.likeMe,false)
            commentAdapter.submitList(state.comments)
            _binding.apply {
                notNull.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                listNull.layoutParams.height = root.resources.getDimension(R.dimen._0sdp).toInt()
                rvSeason.adapter = seasonAdapter
                rvHotComment.adapter = commentAdapter
            }
        } else {
            _binding.apply {
                notNull.layoutParams.height = root.resources.getDimension(R.dimen._0sdp).toInt()
                listNull.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            }
        }
    }

    override fun seasonOnClick(season: Season) {
        findNavController().navigate(actionDetailToChapter(season))
    }

    override fun onLike(comment: Comment) {
        if (rxOAuthManager.isExistsCredentials) {
            _likesItentPublisher.onNext(SeasonMangaIntent.LikeIntent(comment.id))
        } else
            findNavController().navigate(actionDetailToLogin())
    }

    override fun onParentComment(comment: Comment) {
        findNavController().navigate(
            actionDetailToCommentDetail(comment)
        )
    }

    override fun onReport(comment: Comment) {
        findNavController().navigate(actionDetailToReport(comment.id))
    }
}