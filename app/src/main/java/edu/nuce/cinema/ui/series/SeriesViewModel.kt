package edu.nuce.cinema.ui.series

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
class SeriesViewModel @ViewModelInject constructor(
    private val _SeriesActionProcessor:SeriesActionProcessor
): ViewModel(),MviViewModel<SeriesIntent,SeriesViewState>{

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<SeriesIntent> = PublishSubject.create()
    private val statesObservable: Observable<SeriesViewState> = compose()

    override fun processIntents(intents: Observable<SeriesIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<SeriesViewState> = statesObservable

    private fun compose(): Observable<SeriesViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(_SeriesActionProcessor.actionProcessor)
            .scan(SeriesViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: SeriesIntent): SeriesAction {
        return when (intent) {
            is SeriesIntent.GetSeriesIntent -> SeriesAction.GetSeriesAction
            is SeriesIntent.GetSeriesByCategoryIntent -> SeriesAction.GetSeriesByCategoryAction(intent.id)
            is SeriesIntent.GetSeriesLikeIntent -> SeriesAction.GetSeriesLikeAction(intent.input)
            is SeriesIntent.GetSeriesByCategoryLikeIntent -> SeriesAction.GetSeriesByCategoryLikeAction(intent.input,intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: SeriesViewState, result: SeriesResult ->
            when (result) {
                is SeriesResult.GetSeriesResult -> when (result) {
                    is SeriesResult.GetSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            series = result.series
                        )
                    }
                    is SeriesResult.GetSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SeriesResult.GetSeriesResult.InFlight -> previousState.copy(isLoading = true)
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