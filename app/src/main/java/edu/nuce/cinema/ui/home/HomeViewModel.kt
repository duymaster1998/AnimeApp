package edu.nuce.cinema.ui.home

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
class HomeViewModel @ViewModelInject constructor(
    private val actionHomeProcessor: HomeActionProcessor
) : ViewModel(), MviViewModel<HomeIntent, HomeViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<HomeIntent> = PublishSubject.create()
    private val statesObservable: Observable<HomeViewState> = compose()

    override fun processIntents(intents: Observable<HomeIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<HomeViewState> = statesObservable

    private fun compose(): Observable<HomeViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionHomeProcessor.actionProcessor)
            .scan(HomeViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: HomeIntent): HomeAction {
        return when (intent) {
            is HomeIntent.GetAllSeriesIntent -> HomeAction.GetAllSeriesAction
            is HomeIntent.GetTopAnimeIntent -> HomeAction.GetTopAnimeAction
            is HomeIntent.GetTopCategoryIntent->HomeAction.GetTopCategoryAction
            is HomeIntent.GetNewAnimeIntent->HomeAction.GetNewAnimeAction
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: HomeViewState, result: HomeResult ->
            when (result) {
                is HomeResult.GetAllSeriesResult -> when (result) {
                    is HomeResult.GetAllSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            allSeries = result.allSeries
                        )
                    }
                    is HomeResult.GetAllSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is HomeResult.GetAllSeriesResult.InFlight -> previousState.copy(isLoading = true)
                }
                is HomeResult.GetNewAnimeResult -> when (result) {
                    is HomeResult.GetNewAnimeResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            animeN = result.animes
                        )
                    }
                    is HomeResult.GetNewAnimeResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is HomeResult.GetNewAnimeResult.InFlight -> previousState.copy(isLoading = true)
                }
                is HomeResult.GetTopAnimeResult -> when (result) {
                    is HomeResult.GetTopAnimeResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            topAnime = result.topAnime
                        )
                    }
                    is HomeResult.GetTopAnimeResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is HomeResult.GetTopAnimeResult.InFlight -> previousState.copy(isLoading = true)
                }
                is HomeResult.GetTopCategoryResult -> when (result) {
                    is HomeResult.GetTopCategoryResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            categories = result.categories
                        )
                    }
                    is HomeResult.GetTopCategoryResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is HomeResult.GetTopCategoryResult.InFlight -> previousState.copy(isLoading = true)
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