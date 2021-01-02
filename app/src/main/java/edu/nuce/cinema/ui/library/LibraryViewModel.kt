package edu.nuce.cinema.ui.library

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
class LibraryViewModel @ViewModelInject constructor(
    private val actionLibraryProcessor: LibraryActionProcessor
) : ViewModel(), MviViewModel<LibraryIntent, LibraryViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<LibraryIntent> = PublishSubject.create()
    private val statesObservable: Observable<LibraryViewState> = compose()

    override fun processIntents(intents: Observable<LibraryIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<LibraryViewState> = statesObservable

    private fun compose(): Observable<LibraryViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionLibraryProcessor.actionProcessor)
            .scan(LibraryViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: LibraryIntent): LibraryAction {
        return when (intent) {
            is LibraryIntent.GetSubscribeSeriesIntent -> LibraryAction.GetSubscibeSeriesAction
            is LibraryIntent.GetListSaveIntent -> LibraryAction.GetSaveListAction
            is LibraryIntent.AddListSaveIntent -> LibraryAction.AddSaveListAction(intent.name)
            is LibraryIntent.RemoveArchiveIntent -> LibraryAction.RemoveArchiveAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: LibraryViewState, result: LibraryResult ->
            when (result) {
                is LibraryResult.GetSubscribeSeriesResult -> when (result) {
                    is LibraryResult.GetSubscribeSeriesResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            series = result.series
                        )
                    }
                    is LibraryResult.GetSubscribeSeriesResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is LibraryResult.GetSubscribeSeriesResult .InFlight -> previousState.copy(isLoading = true)
                }
                is LibraryResult.GetSaveListResult -> when (result) {
                    is LibraryResult.GetSaveListResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            archives = result.archives
                        )
                    }
                    is LibraryResult.GetSaveListResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is LibraryResult.GetSaveListResult .InFlight -> previousState.copy(isLoading = true)
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