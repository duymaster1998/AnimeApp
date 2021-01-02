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
            is MangaIntent.UpdateMangaIntent -> MangaAction.UpdateMangaAction(
                id = intent.id,
                intent.historyParams
            )
            is MangaIntent.GetSeriesIntent -> MangaAction.GetSeriesAction(id = intent.id)
            is MangaIntent.GetArchiveIntent -> MangaAction.GetArchiveAction(id = intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: MangaViewState, result: MangaResult ->
            when (result) {
                is MangaResult.UpdateMangaResult -> when (result) {
                    is MangaResult.UpdateMangaResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            manga = result.Manga
                        )
                    }
                    is MangaResult.UpdateMangaResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is MangaResult.UpdateMangaResult.InFlight -> previousState.copy(
                        isLoading = true
                    )
                }
                is MangaResult.GetSeriesResult -> when (result) {
                    is MangaResult.GetSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            series = result.series
                        )
                    }
                    is MangaResult.GetSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is MangaResult.GetSeriesResult.InFlight -> previousState.copy(isLoading = true)
                }
                is MangaResult.GetArchiveResult -> when (result) {
                    is MangaResult.GetArchiveResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            archives = result.archives
                        )
                    }
                    is MangaResult.GetArchiveResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is MangaResult.GetArchiveResult.InFlight -> previousState.copy(isLoading = true)
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