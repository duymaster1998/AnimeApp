package edu.nuce.cinema.ui.series

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SeriesActionProcessor @Inject constructor(
    private val _apiService: ApiService,
) {
    private val getSeriesProcessor =
        ObservableTransformer<SeriesAction.GetSeriesAction, SeriesResult.GetSeriesResult> { action ->
            action.flatMap {
                _apiService.getSeries()
                    .map { SeriesResult.GetSeriesResult.Success(series = it) }
                    .cast(SeriesResult.GetSeriesResult::class.java)
                    .onErrorReturn(SeriesResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeriesResult.GetSeriesResult.InFlight)
            }
        }
    private val getSeriesByCategoryActionProcessor =
        ObservableTransformer<SeriesAction.GetSeriesByCategoryAction,SeriesResult.GetSeriesResult>{ action->
            action.flatMap {
                _apiService.getSeriesByCategory(it.id)
                    .map { SeriesResult.GetSeriesResult.Success(series = it) }
                    .cast(SeriesResult.GetSeriesResult::class.java)
                    .onErrorReturn(SeriesResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeriesResult.GetSeriesResult.InFlight)
            }
        }
    private val getSeriesLikeActionProcessor =
        ObservableTransformer<SeriesAction.GetSeriesLikeAction,SeriesResult.GetSeriesResult>{ action->
            action.flatMap {
                _apiService.getSeriesLike(it.input)
                    .map { SeriesResult.GetSeriesResult.Success(series = it) }
                    .cast(SeriesResult.GetSeriesResult::class.java)
                    .onErrorReturn(SeriesResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeriesResult.GetSeriesResult.InFlight)
            }
        }
    private val getSeriesByCategoryLikeActionProcessor =
        ObservableTransformer<SeriesAction.GetSeriesByCategoryLikeAction,SeriesResult.GetSeriesResult>{ action->
            action.flatMap {
                _apiService.getSeriesByCategoryLike(it.input,it.id)
                    .map { SeriesResult.GetSeriesResult.Success(series = it) }
                    .cast(SeriesResult.GetSeriesResult::class.java)
                    .onErrorReturn(SeriesResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SeriesResult.GetSeriesResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<SeriesAction, SeriesResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(SeriesAction.GetSeriesAction::class.java).compose(getSeriesProcessor),
                shared.ofType(SeriesAction.GetSeriesByCategoryAction::class.java).compose(getSeriesByCategoryActionProcessor),
                shared.ofType(SeriesAction.GetSeriesLikeAction::class.java).compose(getSeriesLikeActionProcessor),
                shared.ofType(SeriesAction.GetSeriesByCategoryLikeAction::class.java).compose(getSeriesByCategoryLikeActionProcessor)
            )
        }
    }
}