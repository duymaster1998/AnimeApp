package edu.nuce.cinema.ui.anime.dialog

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
class SaveViewModel @ViewModelInject constructor(
    private val actionSaveProcessor: SaveActionProcessor
) : ViewModel(), MviViewModel<SaveIntent, SaveViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<SaveIntent> = PublishSubject.create()
    private val statesObservable: Observable<SaveViewState> = compose()

    override fun processIntents(intents: Observable<SaveIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<SaveViewState> = statesObservable

    private fun compose(): Observable<SaveViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionSaveProcessor.actionProcessor)
            .scan(SaveViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: SaveIntent): SaveAction {
        return when (intent) {
            is SaveIntent.GetListSaveIntent -> SaveAction.GetSaveListAction
            is SaveIntent.AddListSaveIntent -> SaveAction.AddSaveListAction(intent.name)
            is SaveIntent.StorageIntent -> SaveAction.StorageAction(intent.storageParams)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: SaveViewState, result: SaveResult ->
            when (result) {
                is SaveResult.GetSaveListResult -> when (result) {
                    is SaveResult.GetSaveListResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            archives = result.archives
                        )
                    }
                    is SaveResult.GetSaveListResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SaveResult.GetSaveListResult .InFlight -> previousState.copy(isLoading = true)
                }
                is SaveResult.StorageResult -> when (result) {
                    is SaveResult.StorageResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            mess = result.message
                        )
                    }
                    is SaveResult.StorageResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SaveResult.StorageResult .InFlight -> previousState.copy(isLoading = true)
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