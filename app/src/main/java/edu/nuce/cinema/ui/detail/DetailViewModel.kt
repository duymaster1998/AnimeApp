package edu.nuce.cinema.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import edu.nuce.base.mvi.MviViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

@FragmentScoped
class DetailViewModel @ViewModelInject constructor(
    private val actionDetailActionProcessor: DetailActionProcessor
) : ViewModel(), MviViewModel<DetailIntent, DetailViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<DetailIntent> = PublishSubject.create()
    private val statesObservable: Observable<DetailViewState> = compose()

    override fun processIntents(intents: Observable<DetailIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<DetailViewState> = statesObservable

    private fun compose(): Observable<DetailViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionDetailActionProcessor.actionProcessor)
            .scan(DetailViewState.initialState(), DetailViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: DetailIntent): DetailAction {
        return when (intent) {
            is DetailIntent.GetCategoryBySeriesIntent -> DetailAction.GetCategoryBySeriesAction(id = intent.id)
            is DetailIntent.GetRateSeriesIntent -> DetailAction.GetRateSeriesAction(id = intent.id)
            is DetailIntent.IsFollowIntent -> DetailAction.IsFollowAction(id = intent.id)
            is DetailIntent.FollowIntent -> DetailAction.FollowAction(id = intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: DetailViewState, result: DetailResult ->
            when (result) {
                is DetailResult.GetCategoryBySeriesResult -> when (result) {
                    is DetailResult.GetCategoryBySeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            categories = result.categories
                        )
                    }
                    is DetailResult.GetCategoryBySeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is DetailResult.GetCategoryBySeriesResult.InFlight -> previousState.copy(
                        isLoading = true
                    )
                }
                is DetailResult.GetRateSeriesResult -> when (result) {
                    is DetailResult.GetRateSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            rating = result.rate
                        )
                    }
                    is DetailResult.GetRateSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is DetailResult.GetRateSeriesResult.InFlight -> previousState.copy(isLoading = true)
                }
                is DetailResult.IsFollowResult -> when (result) {
                    is DetailResult.IsFollowResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            isFollow = result.isFollow
                        )
                    }
                    is DetailResult.IsFollowResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is DetailResult.IsFollowResult.InFlight -> previousState.copy(isLoading = true)
                }
                is DetailResult.FollowResult -> when (result) {
                    is DetailResult.FollowResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            message = result.message
                        )
                    }
                    is DetailResult.FollowResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is DetailResult.FollowResult.InFlight -> previousState.copy(isLoading = true)
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