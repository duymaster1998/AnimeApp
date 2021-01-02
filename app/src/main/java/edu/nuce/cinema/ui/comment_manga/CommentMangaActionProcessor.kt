package edu.nuce.cinema.ui.comment_manga

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

class CommentMangaActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getCommentProcessor =
        ObservableTransformer<CommentMangaAction.GetCommentAction,CommentMangaResult.GetCommentResult> { action ->
            action.flatMap {
                apiService.getCommentManga(it.id)
                    .map { CommentMangaResult.GetCommentResult.Success(comments = it) }
                    .cast(CommentMangaResult.GetCommentResult::class.java)
                    .onErrorReturn(CommentMangaResult.GetCommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentMangaResult.GetCommentResult.InFlight)
            }
        }

    private val commentProcessor =
        ObservableTransformer<CommentMangaAction.CommentAction,CommentMangaResult.GetCommentResult> { action ->
            action.flatMap {
                apiService.commentSeries(it.commentParams)
                    .andThen(apiService.getCommentManga(it.id))
                    .map { CommentMangaResult.GetCommentResult.Success(comments = it) }
                    .cast(CommentMangaResult.GetCommentResult::class.java)
                    .onErrorReturn(CommentMangaResult.GetCommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentMangaResult.GetCommentResult.InFlight)
            }
        }
    private val getSeriesProcessor =
        ObservableTransformer<CommentMangaAction.GetSeriesAction, CommentMangaResult.GetSeriesResult> { action ->
            action.flatMap {
                apiService.getSeriesByManga(it.id)
                    .toObservable()
                    .map { CommentMangaResult.GetSeriesResult.Success(series = it) }
                    .cast(CommentMangaResult.GetSeriesResult::class.java)
                    .onErrorReturn(CommentMangaResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentMangaResult.GetSeriesResult.InFlight)
            }
        }
    private val likeMeProcessor =
        ObservableTransformer<CommentMangaAction.GetLikeAction,CommentMangaResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.getLikeMe()
                    .map { CommentMangaResult.LikeMeResult.Success(likes = it) }
                    .cast(CommentMangaResult.LikeMeResult::class.java)
                    .onErrorReturn(CommentMangaResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentMangaResult.LikeMeResult.InFlight)
            }
        }
    private val likeProcessor =
        ObservableTransformer<CommentMangaAction.LikeAction,CommentMangaResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.LikeComment(it.id)
                    .andThen(apiService.getLikeMe())
                    .map { CommentMangaResult.LikeMeResult.Success(likes = it) }
                    .cast(CommentMangaResult.LikeMeResult::class.java)
                    .onErrorReturn(CommentMangaResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(CommentMangaResult.LikeMeResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<CommentMangaAction, CommentMangaResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(CommentMangaAction.GetCommentAction::class.java)
                    .compose(getCommentProcessor),
                shared.ofType(CommentMangaAction.CommentAction::class.java)
                    .compose(commentProcessor),
                shared.ofType(CommentMangaAction.GetSeriesAction::class.java)
                    .compose(getSeriesProcessor),
                shared.ofType(CommentMangaAction.LikeAction::class.java)
                    .compose(likeProcessor)
            ).mergeWith(
                shared.ofType(CommentMangaAction.GetLikeAction::class.java)
                    .compose(likeMeProcessor)
            )
        }
    }
}