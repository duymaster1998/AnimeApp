package edu.nuce.cinema.ui.manga

import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.ui.home.HomeAction
import edu.nuce.cinema.ui.home.HomeResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MangaActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val getCategoryBySeriesProcessor =
        ObservableTransformer<MangaAction.GetCategoryBySeriesAction, MangaResult.GetCategoryBySeriesResult> { action ->
            action.flatMap { mangaAction ->
                apiService.getCategoryBySeries(mangaAction.id)
                    .map { MangaResult.GetCategoryBySeriesResult.Success(categories = it) }
                    .cast(MangaResult.GetCategoryBySeriesResult::class.java)
                    .onErrorReturn(MangaResult.GetCategoryBySeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(MangaResult.GetCategoryBySeriesResult.InFlight)
            }
        }
    private val getRateSeriesProcessor =
        ObservableTransformer<MangaAction.GetRateSeriesAction, MangaResult.GetRateSeriesResult> { action ->
            action.flatMap { mangaAction ->
                apiService.getRateSeries(mangaAction.id)
                    .toObservable()
                    .map { MangaResult.GetRateSeriesResult.Success(rate = it) }
                    .cast(MangaResult.GetRateSeriesResult::class.java)
                    .onErrorReturn(MangaResult.GetRateSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(MangaResult.GetRateSeriesResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<MangaAction, MangaResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(MangaAction.GetCategoryBySeriesAction::class.java)
                    .compose(getCategoryBySeriesProcessor),
                shared.ofType(MangaAction.GetRateSeriesAction::class.java).compose(getRateSeriesProcessor),
            )
        }
    }
}