package edu.nuce.cinema.ui.comment_detail


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
import edu.nuce.cinema.databinding.FragmentCommentDetailBinding
import edu.nuce.cinema.ui.anime.AnimeIntent
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.comment_detail.CommentDetailFragmentDirections.Companion.actionCommentDetailFragmentToLogin
import edu.nuce.cinema.ui.comment_detail.CommentDetailFragmentDirections.Companion.actionCommentDetailToReport
import edu.nuce.cinema.ui.common.adapter.CommentAdapter
import edu.nuce.cinema.ui.common.events.CommentEvent
import edu.nuce.cinema.ui.detail.DetailFragmentDirections
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToLogin
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CommentDetailFragment : BaseFragment(R.layout.fragment_comment_detail),
    MviView<CommentDetailIntent, CommentDetailViewState>, CommentEvent {

    private lateinit var adapter: CommentAdapter

    private val args by navArgs<CommentDetailFragmentArgs>()
    private val _binding by viewBinding<FragmentCommentDetailBinding>()

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager

    private val _getCommentItentPublisher =
        PublishSubject.create<CommentDetailIntent.GetCommentIntent>()
    private val _commentItentPublisher =
        PublishSubject.create<CommentDetailIntent.CommentIntent>()
    private val _getLikesItentPublisher =
        PublishSubject.create<CommentDetailIntent.GetLikeMeIntent>()
    private val _likesItentPublisher =
        PublishSubject.create<CommentDetailIntent.LikeIntent>()
    private val _viewModel: CommentDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        _getCommentItentPublisher.onNext(CommentDetailIntent.GetCommentIntent(args.comment.id))
        _getLikesItentPublisher.onNext(CommentDetailIntent.GetLikeMeIntent)
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<CommentDetailIntent> {
        return Observable.merge(
            _getCommentItentPublisher,
            _commentItentPublisher,
            _getLikesItentPublisher,
            _likesItentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: CommentDetailViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapter = CommentAdapter(this, requestManager, state.likeMe,true)
        adapter.submitList(state.comments)
        _binding.apply {
            include.apply {
                imgAction.setImageDrawable(resources.getDrawable(R.drawable.ic_previous_black))
                imgAction.setOnClickListener { onBackPressed() }
                title.text = resources.getString(R.string.comments_detail)
            }
            rvComment.adapter = adapter
            btnSend.setOnClickListener {
                if (etComment.text != null) {
                    commentSeries(
                        CommentParams(
                            args.comment.id.toInt(),
                            etComment.text.toString(),
                            args.comment.seriesId.toInt(),
                            "ANIME"
                        )
                    )
                    adapter.submitList(state.comments)
                    etComment.text.clear()
                    hideKeyboard()
                }
            }
            val name = args.comment.firstName + " " + args.comment.lastName
            parentComment.apply {
                ivReport.visibility = View.GONE
                ivCmt.visibility = View.GONE
                btnLike.visibility = View.GONE
                requestManager.load(args.comment.avatar)
                    .circleCrop()
                    .into(avatar)
                tvName.text = name
                tvComment.text = args.comment.content
                tvDate.visibility = View.GONE
                tvLike.visibility = View.GONE
            }
        }
    }

    fun commentSeries(commentParams: CommentParams) {
        if (rxOAuthManager.isExistsCredentials) {
            _commentItentPublisher.onNext(
                CommentDetailIntent.CommentIntent(
                    commentParams,
                    args.comment.id
                )
            )
            toast("Comment success")
        } else
            findNavController().navigate(actionCommentDetailFragmentToLogin())
    }

    override fun onLike(comment: Comment) {
        if (rxOAuthManager.isExistsCredentials) {
            _likesItentPublisher.onNext(CommentDetailIntent.LikeIntent(comment.id))
        } else
            findNavController().navigate(actionDetailToLogin())
    }

    override fun onParentComment(comment: Comment) {
        TODO("Not yet implemented")
    }

    override fun onReport(comment: Comment) {
        findNavController().navigate(actionCommentDetailToReport(comment.id))
    }
}
