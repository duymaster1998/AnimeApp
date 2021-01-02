package edu.nuce.cinema.ui.comment_detail

import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.ui.anime.AnimeAction
import edu.nuce.cinema.ui.anime.AnimeResult
import edu.nuce.cinema.ui.episode.EpisodeAction
import edu.nuce.cinema.ui.episode.EpisodeResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CommentDetailActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getCommentProcessor =
        ObservableTransformer<CommentDetailAction.GetCommentAction,CommentDetailResult.GetCommentResult> { action ->
            action.flatMap {
                apiService.getChildCommentAnime(it.id)
                    .map { CommentDetailResult.GetCommentResult.Success(comments = it) }
                    .cast(CommentDetailResult.GetCommentResult::class.java)
                    .onErrorReturn(CommentDetailResult.GetCommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentDetailResult.GetCommentResult.InFlight)
            }
        }

    private val commentProcessor =
        ObservableTransformer<CommentDetailAction.CommentAction,CommentDetailResult.GetCommentResult> { action ->
            action.flatMap {
                apiService.commentSeries(it.commentParams)
                    .andThen(apiService.getChildCommentAnime(it.id))
                    .map { CommentDetailResult.GetCommentResult.Success(comments = it) }
                    .cast(CommentDetailResult.GetCommentResult::class.java)
                    .onErrorReturn(CommentDetailResult.GetCommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentDetailResult.GetCommentResult.InFlight)
            }
        }
    private val likeMeProcessor =
        ObservableTransformer<CommentDetailAction.GetLikeAction, CommentDetailResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.getLikeMe()
                    .map { CommentDetailResult.LikeMeResult.Success(likes = it) }
                    .cast(CommentDetailResult.LikeMeResult::class.java)
                    .onErrorReturn(CommentDetailResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentDetailResult.LikeMeResult.InFlight)
            }
        }
    private val likeProcessor =
        ObservableTransformer<CommentDetailAction.LikeAction, CommentDetailResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.LikeComment(it.id)
                    .andThen(apiService.getLikeMe())
                    .map { CommentDetailResult.LikeMeResult.Success(likes = it) }
                    .cast(CommentDetailResult.LikeMeResult::class.java)
                    .onErrorReturn(CommentDetailResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentDetailResult.LikeMeResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<CommentDetailAction, CommentDetailResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(CommentDetailAction.GetCommentAction::class.java)
                    .compose(getCommentProcessor),
                shared.ofType(CommentDetailAction.CommentAction::class.java)
                    .compose(commentProcessor),
                shared.ofType(CommentDetailAction.GetLikeAction::class.java)
                    .compose(likeMeProcessor),
                shared.ofType(CommentDetailAction.LikeAction::class.java)
                    .compose(likeProcessor)
            )
        }
    }
}