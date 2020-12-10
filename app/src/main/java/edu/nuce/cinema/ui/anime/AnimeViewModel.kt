package edu.nuce.cinema.ui.anime

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import edu.nuce.base.mvi.MviViewModel
import edu.nuce.cinema.ui.home.HomeAction
import edu.nuce.cinema.ui.home.HomeIntent
import edu.nuce.cinema.ui.home.HomeResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

@FragmentScoped
class AnimeViewModel @ViewModelInject constructor(
    private val actionAnimeActionProcessor: AnimeActionProcessor
) : ViewModel(), MviViewModel<AnimeIntent, AnimeViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<AnimeIntent> = PublishSubject.create()
    private val statesObservable: Observable<AnimeViewState> = compose()

    override fun processIntents(intents: Observable<AnimeIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<AnimeViewState> = statesObservable

    private fun compose(): Observable<AnimeViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionAnimeActionProcessor.actionProcessor)
            .scan(AnimeViewState.initialState(), AnimeViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: AnimeIntent): AnimeAction {
        return when (intent) {
            is AnimeIntent.GetCategoryBySeriesIntent -> AnimeAction.GetCategoryBySeriesAction(id = intent.id)
            is AnimeIntent.GetRateSeriesIntent -> AnimeAction.GetRateSeriesAction(id = intent.id)
            is AnimeIntent.GetSeriesIntent -> AnimeAction.GetSeriesAction(id = intent.id)
            is AnimeIntent.GetSeriesAIntent -> AnimeAction.GetSeriesAAction(id = intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: AnimeViewState, result: AnimeResult ->
            when (result) {
                is AnimeResult.GetCategoryBySeriesResult -> when (result) {
                    is AnimeResult.GetCategoryBySeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            categories = result.categories
                        )
                    }
                    is AnimeResult.GetCategoryBySeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.GetCategoryBySeriesResult.InFlight -> previousState.copy(
                        isLoading = true
                    )
                }
                is AnimeResult.GetRateSeriesResult -> when (result) {
                    is AnimeResult.GetRateSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            rating = result.rate
                        )
                    }
                    is AnimeResult.GetRateSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.GetRateSeriesResult.InFlight -> previousState.copy(isLoading = true)
                }
                is AnimeResult.GetSeriesResult -> when (result) {
                    is AnimeResult.GetSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            series = result.series
                        )
                    }
                    is AnimeResult.GetSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.GetSeriesResult.InFlight -> previousState.copy(isLoading = true)
                }
                is AnimeResult.GetSeriesAResult -> when (result) {
                    is AnimeResult.GetSeriesAResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            series = result.series
                        )
                    }
                    is AnimeResult.GetSeriesAResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.GetSeriesAResult.InFlight -> previousState.copy(isLoading = true)
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