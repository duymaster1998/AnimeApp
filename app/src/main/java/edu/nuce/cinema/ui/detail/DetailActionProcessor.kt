package edu.nuce.cinema.ui.detail

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DetailActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val getCategoryBySeriesProcessor =
        ObservableTransformer<DetailAction.GetCategoryBySeriesAction, DetailResult.GetCategoryBySeriesResult> { action ->
            action.flatMap { detaiAction ->
                apiService.getCategoryBySeries(detaiAction.id)
                    .map { DetailResult.GetCategoryBySeriesResult.Success(categories = it) }
                    .cast(DetailResult.GetCategoryBySeriesResult::class.java)
                    .onErrorReturn(DetailResult.GetCategoryBySeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DetailResult.GetCategoryBySeriesResult.InFlight)
            }
        }
    private val getRateSeriesProcessor =
        ObservableTransformer<DetailAction.GetRateSeriesAction, DetailResult.GetRateSeriesResult> { action ->
            action.flatMap { detaiAction ->
                apiService.getRateSeries(detaiAction.id)
                    .toObservable()
                    .map { DetailResult.GetRateSeriesResult.Success(rate = it) }
                    .cast(DetailResult.GetRateSeriesResult::class.java)
                    .onErrorReturn(DetailResult.GetRateSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DetailResult.GetRateSeriesResult.InFlight)
            }
        }
    private val IsFollowProcessor =
        ObservableTransformer<DetailAction.IsFollowAction, DetailResult.IsFollowResult> { action ->
            action.flatMap { detaiAction ->
                apiService.isFollow(detaiAction.id)
                    .toObservable()
                    .map { DetailResult.IsFollowResult.Success(isFollow = it) }
                    .cast(DetailResult.IsFollowResult::class.java)
                    .onErrorReturn(DetailResult.IsFollowResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DetailResult.IsFollowResult.InFlight)
            }
        }
    private val FollowProcessor =
        ObservableTransformer<DetailAction.FollowAction, DetailResult.FollowResult> { action ->
            action.flatMap {
                apiService.createFollow(it.id).andThen(
                    apiService.isFollow(it.id))
                    .toObservable()
                    .map { DetailResult.FollowResult.Success(isFollow = it) }
                    .cast(DetailResult.FollowResult::class.java)
                    .onErrorReturn(DetailResult.FollowResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DetailResult.FollowResult.InFlight)
            }
        }
    private val UnFollowProcessor =
        ObservableTransformer<DetailAction.UnFollowAction, DetailResult.UnFollowResult> { action ->
            action.flatMap {
                apiService.unFollow(it.id)
                    .andThen(
                    apiService.isFollow(it.id))
                    .toObservable()
                    .map { DetailResult.UnFollowResult.Success(isFollow = it) }
                    .cast(DetailResult.UnFollowResult::class.java)
                    .onErrorReturn(DetailResult.UnFollowResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DetailResult.UnFollowResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<DetailAction, DetailResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(DetailAction.GetCategoryBySeriesAction::class.java)
                    .compose(getCategoryBySeriesProcessor),
                shared.ofType(DetailAction.GetRateSeriesAction::class.java)
                    .compose(getRateSeriesProcessor),
                shared.ofType(DetailAction.IsFollowAction::class.java)
                    .compose(IsFollowProcessor),
                shared.ofType(DetailAction.FollowAction::class.java)
                    .compose(FollowProcessor),
            ).mergeWith(
                shared.ofType(DetailAction.UnFollowAction::class.java)
                    .compose(UnFollowProcessor),
            )
        }
    }
}