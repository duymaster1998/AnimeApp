package edu.nuce.cinema.ui.manga

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
class MangaViewModel @ViewModelInject constructor(
    private val actionMangaActionProcessor: MangaActionProcessor
) : ViewModel(), MviViewModel<MangaIntent, MangaViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<MangaIntent> = PublishSubject.create()
    private val statesObservable: Observable<MangaViewState> = compose()

    override fun processIntents(intents: Observable<MangaIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<MangaViewState> = statesObservable

    private fun compose(): Observable<MangaViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionMangaActionProcessor.actionProcessor)
            .scan(MangaViewState.initialState(), MangaViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: MangaIntent): MangaAction {
        return when (intent) {
            is MangaIntent.GetCategoryBySeriesIntent -> MangaAction.GetCategoryBySeriesAction(id = intent.id)
            is MangaIntent.GetRateSeriesIntent -> MangaAction.GetRateSeriesAction(id = intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: MangaViewState, result: MangaResult ->
            when (result) {
                is MangaResult.GetCategoryBySeriesResult -> when (result) {
                    is MangaResult.GetCategoryBySeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            categories = result.categories
                        )
                    }
                    is MangaResult.GetCategoryBySeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is MangaResult.GetCategoryBySeriesResult.InFlight -> previousState.copy(
                        isLoading = true
                    )
                }
                is MangaResult.GetRateSeriesResult -> when (result) {
                    is MangaResult.GetRateSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            rating = result.rate
                        )
                    }
                    is MangaResult.GetRateSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is MangaResult.GetRateSeriesResult.InFlight -> previousState.copy(isLoading = true)
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