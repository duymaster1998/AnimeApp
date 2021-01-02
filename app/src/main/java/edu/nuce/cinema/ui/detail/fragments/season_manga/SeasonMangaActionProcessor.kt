package edu.nuce.cinema.ui.detail.fragments.season_manga

import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeAction
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SeasonMangaActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val getSeasonsProcessor =
        ObservableTransformer<SeasonMangaAction.GetSeasonsAction, SeasonMangaResult.GetSeasonsResult> { action ->
            action.flatMap { episodeAction ->
                apiService.getSeasonManga(episodeAction.id)
                    .map { SeasonMangaResult.GetSeasonsResult.Success(seasons = it) }
                    .cast(SeasonMangaResult.GetSeasonsResult::class.java)
                    .onErrorReturn(SeasonMangaResult.GetSeasonsResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonMangaResult.GetSeasonsResult.InFlight)
            }
        }
    private val getTopCommentProcessor =
        ObservableTransformer<SeasonMangaAction.GetTopCommentAction, SeasonMangaResult.GetTopCommentResult> { action ->
            action.flatMap { episodeAction ->
                apiService.getTopCommentManga(episodeAction.id)
                    .map { SeasonMangaResult.GetTopCommentResult.Success(comments = it) }
                    .cast(SeasonMangaResult.GetTopCommentResult::class.java)
                    .onErrorReturn(SeasonMangaResult.GetTopCommentResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonMangaResult.GetTopCommentResult.InFlight)
            }
        }
    private val likeMeProcessor =
        ObservableTransformer<SeasonMangaAction.GetLikeAction, SeasonMangaResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.getLikeMe()
                    .map { SeasonMangaResult.LikeMeResult.Success(likes = it) }
                    .cast(SeasonMangaResult.LikeMeResult::class.java)
                    .onErrorReturn(SeasonMangaResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonMangaResult.LikeMeResult.InFlight)
            }
        }
    private val likeProcessor =
        ObservableTransformer<SeasonMangaAction.LikeAction, SeasonMangaResult.LikeMeResult> { action ->
            action.flatMap {
                apiService.LikeComment(it.id)
                    .andThen(apiService.getLikeMe())
                    .map { SeasonMangaResult.LikeMeResult.Success(likes = it) }
                    .cast(SeasonMangaResult.LikeMeResult::class.java)
                    .onErrorReturn(SeasonMangaResult.LikeMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeasonMangaResult.LikeMeResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<SeasonMangaAction, SeasonMangaResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(SeasonMangaAction.GetSeasonsAction::class.java)
                    .compose(getSeasonsProcessor),
                shared.ofType(SeasonMangaAction.GetTopCommentAction::class.java)
                    .compose(getTopCommentProcessor),
                shared.ofType(SeasonMangaAction.GetLikeAction::class.java)
                    .compose(likeMeProcessor),
                shared.ofType(SeasonMangaAction.LikeAction::class.java)
                    .compose(likeProcessor),
            )
        }
    }
}