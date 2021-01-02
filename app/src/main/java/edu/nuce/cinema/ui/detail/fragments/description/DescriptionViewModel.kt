package edu.nuce.cinema.ui.Description.fragments.description

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import edu.nuce.base.mvi.MviViewModel
import edu.nuce.cinema.ui.detail.fragments.description.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

@FragmentScoped
class DescriptionViewModel @ViewModelInject constructor(
    private val actionDescriptionActionProcessor: DescriptionActionProcessor
) : ViewModel(), MviViewModel<DescriptionIntent, DescriptionViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<DescriptionIntent> = PublishSubject.create()
    private val statesObservable: Observable<DescriptionViewState> = compose()

    override fun processIntents(intents: Observable<DescriptionIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<DescriptionViewState> = statesObservable

    private fun compose(): Observable<DescriptionViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionDescriptionActionProcessor.actionProcessor)
            .scan(DescriptionViewState.initialState(), DescriptionViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: DescriptionIntent): DescriptionAction {
        return when (intent) {
            is DescriptionIntent.GetRecommendIntent -> DescriptionAction.GetRecommendAction(id = intent.id)
            is DescriptionIntent.GetRateSeriesIntent -> DescriptionAction.GetRateSeriesAction(id = intent.id)
            is DescriptionIntent.GetAuthorIntent -> DescriptionAction.GetAuthorAction(id = intent.id)
            is DescriptionIntent.GetRateMeIntent -> DescriptionAction.GetRateMeAction(id = intent.id)
            is DescriptionIntent.RatingIntent -> DescriptionAction.RatingAction(rating = intent.rating,intent.id)
        }
    }

    companion object {
        private val reducer =
            BiFunction { previousState: DescriptionViewState, result: DescriptionResult ->
                when (result) {
                    is DescriptionResult.GetRecommendResult -> when (result) {
                        is DescriptionResult.GetRecommendResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                series = result.series
                            )
                        }
                        is DescriptionResult.GetRecommendResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is DescriptionResult.GetRecommendResult.InFlight -> previousState.copy(
                            isLoading = true
                        )
                    }
                    is DescriptionResult.GetRateSeriesResult -> when (result) {
                        is DescriptionResult.GetRateSeriesResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                rating = result.rate
                            )
                        }
                        is DescriptionResult.GetRateSeriesResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is DescriptionResult.GetRateSeriesResult.InFlight -> previousState.copy(
                            isLoading = true
                        )
                    }
                    is DescriptionResult.GetAuthorResult -> when (result) {
                        is DescriptionResult.GetAuthorResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                authors = result.authors
                            )
                        }
                        is DescriptionResult.GetAuthorResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is DescriptionResult.GetAuthorResult.InFlight -> previousState.copy(
                            isLoading = true
                        )
                    }
                    is DescriptionResult.GetRateMeResult -> when (result) {
                        is DescriptionResult.GetRateMeResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                rateMe = result.rateMe
                            )
                        }
                        is DescriptionResult.GetRateMeResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is DescriptionResult.GetRateMeResult.InFlight -> previousState.copy(
                            isLoading = true
                        )
                    }
                }
            }
    }

    private fun Disposable.disposeOnCleared(): Disposable {
        disposables.add(this)
        return this
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}