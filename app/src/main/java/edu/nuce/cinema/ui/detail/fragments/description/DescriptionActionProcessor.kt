package edu.nuce.cinema.ui.detail.fragments.description

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DescriptionActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val getRecommendProcessor =
        ObservableTransformer<DescriptionAction.GetRecommendAction, DescriptionResult.GetRecommendResult> { action ->
            action.flatMap { descriptionAction ->
                apiService.getRecommendSeries(descriptionAction.id)
                    .map { DescriptionResult.GetRecommendResult.Success(series = it) }
                    .cast(DescriptionResult.GetRecommendResult::class.java)
                    .onErrorReturn(DescriptionResult.GetRecommendResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DescriptionResult.GetRecommendResult.InFlight)
            }
        }
    private val getRateSeriesProcessor =
        ObservableTransformer<DescriptionAction.GetRateSeriesAction, DescriptionResult.GetRateSeriesResult> { action ->
            action.flatMap { descriptionAction ->
                apiService.getRateSeries(descriptionAction.id)
                    .toObservable()
                    .map { DescriptionResult.GetRateSeriesResult.Success(rate = it) }
                    .cast(DescriptionResult.GetRateSeriesResult::class.java)
                    .onErrorReturn(DescriptionResult.GetRateSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DescriptionResult.GetRateSeriesResult.InFlight)
            }
        }
    private val getAuthorProcessor =
        ObservableTransformer<DescriptionAction.GetAuthorAction, DescriptionResult.GetAuthorResult> { action ->
            action.flatMap {
                apiService.getAuhorBySeries(it.id)
                    .map { DescriptionResult.GetAuthorResult.Success(authors = it) }
                    .cast(DescriptionResult.GetAuthorResult::class.java)
                    .onErrorReturn(DescriptionResult.GetAuthorResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DescriptionResult.GetAuthorResult.InFlight)
            }
        }
    private val getRateMeProcessor =
        ObservableTransformer<DescriptionAction.GetRateMeAction, DescriptionResult.GetRateMeResult> { action ->
            action.flatMap {
                apiService.getRateMe(it.id)
                    .toObservable()
                    .map { DescriptionResult.GetRateMeResult.Success(rateMe = it) }
                    .cast(DescriptionResult.GetRateMeResult::class.java)
                    .onErrorReturn(DescriptionResult.GetRateMeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DescriptionResult.GetRateMeResult.InFlight)
            }
        }
    private val ratingProcessor =
        ObservableTransformer<DescriptionAction.RatingAction, DescriptionResult.GetRateSeriesResult> { action ->
            action.flatMap {
                apiService.ratingSeries(it.rating)
                    .andThen(apiService.getRateSeries(it.id))
                    .toObservable()
                    .map { DescriptionResult.GetRateSeriesResult.Success(rate = it) }
                    .cast(DescriptionResult.GetRateSeriesResult::class.java)
                    .onErrorReturn(DescriptionResult.GetRateSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(DescriptionResult.GetRateSeriesResult.InFlight)
            }
        }
    internal var actionProcessor =
        ObservableTransformer<DescriptionAction, DescriptionResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(DescriptionAction.GetRecommendAction::class.java)
                        .compose(getRecommendProcessor),
                    shared.ofType(DescriptionAction.GetRateSeriesAction::class.java)
                        .compose(getRateSeriesProcessor),
                    shared.ofType(DescriptionAction.GetAuthorAction::class.java)
                        .compose(getAuthorProcessor),
                    shared.ofType(DescriptionAction.GetRateMeAction::class.java)
                        .compose(getRateMeProcessor),
                ).mergeWith(
                    shared.ofType(DescriptionAction.RatingAction::class.java)
                        .compose(ratingProcessor),
                )
            }
        }
}