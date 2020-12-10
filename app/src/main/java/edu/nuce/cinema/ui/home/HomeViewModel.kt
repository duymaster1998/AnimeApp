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
            is HomeIntent.GetNewSeriesIntent -> HomeAction.GetNewSeriesAction
            is HomeIntent.GetTopAnimeIntent -> HomeAction.GetTopAnimeAction
            is HomeIntent.GetTopMangaIntent -> HomeAction.GetTopMangaAction
//            is HomeIntent.GetSeriesIntent -> HomeAction.GetSeriesAction(id = intent.id)
            is HomeIntent.GetTopCategoryIntent->HomeAction.GetTopCategoryAction
            is HomeIntent.GetNewAnimeIntent->HomeAction.GetNewAnimeAction
            is HomeIntent.GetNewMangaIntent->HomeAction.GetNewMangaAction
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: HomeViewState, result: HomeResult ->
            when (result) {
                is HomeResult.GetNewSeriesResult -> when (result) {
                    is HomeResult.GetNewSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            newSeries = result.newSeries
                        )
                    }
                    is HomeResult.GetNewSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is HomeResult.GetNewSeriesResult.InFlight -> previousState.copy(isLoading = true)
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
                is HomeResult.GetTopMangaResult -> when (result) {
                    is HomeResult.GetTopMangaResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            topManga = result.topManga
                        )
                    }
                    is HomeResult.GetTopMangaResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is HomeResult.GetTopMangaResult.InFlight -> previousState.copy(isLoading = true)
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
                is HomeResult.GetNewMangaResult -> when (result) {
                    is HomeResult.GetNewMangaResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            mangaN = result.mangas
                        )
                    }
                    is HomeResult.GetNewMangaResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is HomeResult.GetNewMangaResult.InFlight -> previousState.copy(isLoading = true)
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