package edu.nuce.cinema.ui.anime

import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.ui.home.HomeAction
import edu.nuce.cinema.ui.home.HomeResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AnimeActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val getCategoryBySeriesProcessor =
        ObservableTransformer<AnimeAction.GetCategoryBySeriesAction, AnimeResult.GetCategoryBySeriesResult> { action ->
            action.flatMap { animeAction ->
                apiService.getCategoryBySeries(animeAction.id)
                    .map { AnimeResult.GetCategoryBySeriesResult.Success(categories = it) }
                    .cast(AnimeResult.GetCategoryBySeriesResult::class.java)
                    .onErrorReturn(AnimeResult.GetCategoryBySeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.GetCategoryBySeriesResult.InFlight)
            }
        }
    private val getRateSeriesProcessor =
        ObservableTransformer<AnimeAction.GetRateSeriesAction, AnimeResult.GetRateSeriesResult> { action ->
            action.flatMap { animeAction ->
                apiService.getRateSeries(animeAction.id)
                    .toObservable()
                    .map { AnimeResult.GetRateSeriesResult.Success(rate = it) }
                    .cast(AnimeResult.GetRateSeriesResult::class.java)
                    .onErrorReturn(AnimeResult.GetRateSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.GetRateSeriesResult.InFlight)
            }
        }
    private val getSeriesProcessor =
        ObservableTransformer<AnimeAction.GetSeriesAction, AnimeResult.GetSeriesResult> { action ->
            action.flatMap { animeAction ->
                apiService.getSeriesByAnime(animeAction.id)
                    .toObservable()
                    .map { AnimeResult.GetSeriesResult.Success(series = it) }
                    .cast(AnimeResult.GetSeriesResult::class.java)
                    .onErrorReturn(AnimeResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.GetSeriesResult.InFlight)
            }
        }
    private val getSeriesAProcessor =
        ObservableTransformer<AnimeAction.GetSeriesAAction, AnimeResult.GetSeriesAResult> { action ->
            action.flatMap { animeAction ->
                apiService.getSeriesById(animeAction.id)
                    .toObservable()
                    .map { AnimeResult.GetSeriesAResult.Success(series = it) }
                    .cast(AnimeResult.GetSeriesAResult::class.java)
                    .onErrorReturn(AnimeResult.GetSeriesAResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(AnimeResult.GetSeriesAResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<AnimeAction, AnimeResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(AnimeAction.GetCategoryBySeriesAction::class.java)
                    .compose(getCategoryBySeriesProcessor),
                shared.ofType(AnimeAction.GetRateSeriesAction::class.java).compose(getRateSeriesProcessor),
                shared.ofType(AnimeAction.GetSeriesAction::class.java)
                    .compose(getSeriesProcessor),
            )
        }
    }
}