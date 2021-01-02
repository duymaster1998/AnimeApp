package edu.nuce.cinema.ui.detail.fragments.season_anime

import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.ui.anime.AnimeAction
import edu.nuce.cinema.ui.anime.AnimeResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SeasonAnimeActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val getSeasonsProcessor =
        ObservableTransformer<SeasonAnimeAction.GetSeasonsAction, SeasonAnimeResult.GetSeasonsResult> { action ->
            action.flatMap { episodeAction ->
                apiService.getSeasonAnime(episodeAction.id)
                    .map { SeasonAnimeResult.GetSeasonsResult.Success(seasons = it) }
                    .cast(SeasonAnimeResult.GetSeasonsResult::class.java)
                    .onErrorReturn(SeasonAnimeResult.GetSeasonsResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonAnimeResult.GetSeasonsResult.InFlight)
            }
        }
    private val getTopCommentProcessor =
        ObservableTransformer<SeasonAnimeAction.GetTopCommentAction, SeasonAnimeResult.GetTopCommentResult> { action ->
            action.flatMap { episodeAction ->
                apiService.getTopCommentAnime(episodeAction.id)
                    .map { SeasonAnimeResult.GetTopCommentResult.Success(comments = it) }
                    .cast(SeasonAnimeResult.GetTopCommentResult::class.java)
                    .onErrorReturn(SeasonAnimeResult.GetTopCommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonAnimeResult.GetTopCommentResult.InFlight)
            }
        }
    private val likeMeProcessor =
        ObservableTransformer<SeasonAnimeAction.GetLikeAction, SeasonAnimeResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.getLikeMe()
                    .map { SeasonAnimeResult.LikeMeResult.Success(likes = it) }
                    .cast(SeasonAnimeResult.LikeMeResult::class.java)
                    .onErrorReturn(SeasonAnimeResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonAnimeResult.LikeMeResult.InFlight)
            }
        }
    private val likeProcessor =
        ObservableTransformer<SeasonAnimeAction.LikeAction, SeasonAnimeResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.LikeComment(it.id)
                    .andThen(apiService.getLikeMe())
                    .map { SeasonAnimeResult.LikeMeResult.Success(likes = it) }
                    .cast(SeasonAnimeResult.LikeMeResult::class.java)
                    .onErrorReturn(SeasonAnimeResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonAnimeResult.LikeMeResult.InFlight)
            }
        }
    internal var actionProcessor =
        ObservableTransformer<SeasonAnimeAction, SeasonAnimeResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(SeasonAnimeAction.GetSeasonsAction::class.java)
                        .compose(getSeasonsProcessor),
                    shared.ofType(SeasonAnimeAction.GetTopCommentAction::class.java)
                        .compose(getTopCommentProcessor),
                    shared.ofType(SeasonAnimeAction.GetLikeAction::class.java)
                        .compose(likeMeProcessor),
                    shared.ofType(SeasonAnimeAction.LikeAction::class.java)
                        .compose(likeProcessor),
                )
            }
        }
}