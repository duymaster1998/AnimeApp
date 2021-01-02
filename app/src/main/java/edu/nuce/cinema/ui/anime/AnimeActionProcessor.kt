package edu.nuce.cinema.ui.anime

import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.ui.episode.EpisodeAction
import edu.nuce.cinema.ui.episode.EpisodeResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AnimeActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val updateAnimeProcessor =
        ObservableTransformer<AnimeAction.UpdateAnimeAction, AnimeResult.UpdateAnimeResult> { action ->
            action.flatMap {
                apiService.historySeries(it.historyParams)
                    .andThen(apiService.updateViewAnime(it.id))
                    .toObservable()
                    .map { AnimeResult.UpdateAnimeResult.Success(anime = it) }
                    .cast(AnimeResult.UpdateAnimeResult::class.java)
                    .onErrorReturn(AnimeResult.UpdateAnimeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.UpdateAnimeResult.InFlight)
            }
        }
    private val getTopCommentProcessor =
        ObservableTransformer<AnimeAction.GetCommentAction,AnimeResult.GetCommentResult> { action ->
            action.flatMap { episodeAction ->
                apiService.getCommentAnime(episodeAction.id)
                    .map { AnimeResult.GetCommentResult.Success(comments = it) }
                    .cast(AnimeResult.GetCommentResult::class.java)
                    .onErrorReturn(AnimeResult.GetCommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.GetCommentResult.InFlight)
            }
        }
    private val getSeriesProcessor =
        ObservableTransformer<AnimeAction.GetSeriesAction,AnimeResult.GetSeriesResult> { action ->
            action.flatMap {
                apiService.getSeriesByAnime(it.id)
                    .toObservable()
                    .map { AnimeResult.GetSeriesResult.Success(series = it) }
                    .cast(AnimeResult.GetSeriesResult::class.java)
                    .onErrorReturn(AnimeResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.GetSeriesResult.InFlight)
            }
        }
    private val getArchiveProcessor =
        ObservableTransformer<AnimeAction.GetArchiveAction,AnimeResult.GetArchiveResult> { action ->
            action.flatMap {
                apiService.getArchiveOfAnime(it.id)
                    .map { AnimeResult.GetArchiveResult.Success( archives = it) }
                    .cast(AnimeResult.GetArchiveResult::class.java)
                    .onErrorReturn(AnimeResult.GetArchiveResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.GetArchiveResult.InFlight)
            }
        }
    private val commentProcessor =
        ObservableTransformer<AnimeAction.CommentAction,AnimeResult.CommentResult> { action ->
            action.flatMap {
                apiService.commentSeries(it.commentParams)
                    .andThen(apiService.getCommentAnime(it.id))
                    .map { AnimeResult.CommentResult.Success(comments = it) }
                    .cast(AnimeResult.CommentResult::class.java)
                    .onErrorReturn(AnimeResult.CommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.CommentResult.InFlight)
            }
        }
    private val likeMeProcessor =
        ObservableTransformer<AnimeAction.GetLikeAction,AnimeResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.getLikeMe()
                    .map { AnimeResult.LikeMeResult.Success(likes = it) }
                    .cast(AnimeResult.LikeMeResult::class.java)
                    .onErrorReturn(AnimeResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.LikeMeResult.InFlight)
            }
        }
    private val likeProcessor =
        ObservableTransformer<AnimeAction.LikeAction,AnimeResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.LikeComment(it.id)
                    .andThen(apiService.getLikeMe())
                    .map { AnimeResult.LikeMeResult.Success(likes = it) }
                    .cast(AnimeResult.LikeMeResult::class.java)
                    .onErrorReturn(AnimeResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.LikeMeResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<AnimeAction, AnimeResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(AnimeAction.UpdateAnimeAction::class.java)
                    .compose(updateAnimeProcessor),
                shared.ofType(AnimeAction.GetCommentAction::class.java)
                    .compose(getTopCommentProcessor),
                shared.ofType(AnimeAction.CommentAction::class.java)
                    .compose(commentProcessor),
                shared.ofType(AnimeAction.GetSeriesAction::class.java)
                    .compose(getSeriesProcessor),
            ).mergeWith(
                shared.ofType(AnimeAction.GetArchiveAction::class.java)
                    .compose(getArchiveProcessor)
            ).mergeWith(
                shared.ofType(AnimeAction.GetLikeAction::class.java)
                    .compose(likeMeProcessor)
            ).mergeWith(
                shared.ofType(AnimeAction.LikeAction::class.java)
                    .compose(likeProcessor)
            )
        }
    }
}