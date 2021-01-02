package edu.nuce.cinema.ui.episode

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EpisodeActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getEpisodeAnimeProcessor =
        ObservableTransformer<EpisodeAction.GetEpisodeAnimeAction, EpisodeResult.GetEpisodeAnimeResult> { action ->
            action.flatMap { episodeAction ->
                apiService.getAnimeBySeason(episodeAction.id)
                    .map { EpisodeResult.GetEpisodeAnimeResult.Success(animes = it) }
                    .cast(EpisodeResult.GetEpisodeAnimeResult::class.java)
                    .onErrorReturn(EpisodeResult.GetEpisodeAnimeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(EpisodeResult.GetEpisodeAnimeResult.InFlight)
            }
        }
    private val updateAnimeProcessor =
        ObservableTransformer<EpisodeAction.UpdateAnimeAction, EpisodeResult.UpdateAnimeResult> { action ->
            action.flatMap {
                apiService.updateViewAnime(it.id)
                    .toObservable()
                    .map { EpisodeResult.UpdateAnimeResult.Success(anime = it) }
                    .cast(EpisodeResult.UpdateAnimeResult::class.java)
                    .onErrorReturn(EpisodeResult.UpdateAnimeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(EpisodeResult.UpdateAnimeResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<EpisodeAction, EpisodeResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(EpisodeAction.GetEpisodeAnimeAction::class.java)
                    .compose(getEpisodeAnimeProcessor),
                shared.ofType(EpisodeAction.UpdateAnimeAction::class.java)
                    .compose(updateAnimeProcessor)
            )
        }
    }
}