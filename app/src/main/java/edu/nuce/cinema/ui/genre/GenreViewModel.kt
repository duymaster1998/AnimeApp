package edu.nuce.cinema.ui.genre

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
class GenreViewModel @ViewModelInject constructor(
    private val _GenreActionProcessor:GenreActionProcessor
): ViewModel(),MviViewModel<GenreIntent,GenreViewState>{

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<GenreIntent> = PublishSubject.create()
    private val statesObservable: Observable<GenreViewState> = compose()

    override fun processIntents(intents: Observable<GenreIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<GenreViewState> = statesObservable

    private fun compose(): Observable<GenreViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(_GenreActionProcessor.actionProcessor)
            .scan(GenreViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: GenreIntent): GenreAction {
        return when (intent) {
            is GenreIntent.GetGenreIntent -> GenreAction.GetGenreAction
            is GenreIntent.GetGenreByLikeIntent -> GenreAction.GetGenreByLikeAction(intent.input)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: GenreViewState, result: GenreResult ->
            when (result) {
                is GenreResult.GetGenreResult -> when (result) {
                    is GenreResult.GetGenreResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            genres = result.genres
                        )
                    }
                    is GenreResult.GetGenreResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is GenreResult.GetGenreResult.InFlight -> previousState.copy(isLoading = true)
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