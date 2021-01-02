package edu.nuce.cinema.ui.LibraryDetail_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import edu.nuce.base.mvi.MviViewModel
import edu.nuce.cinema.ui.library_detail.LibraryDetailAction
import edu.nuce.cinema.ui.library_detail.LibraryDetailIntent
import edu.nuce.cinema.ui.library_detail.LibraryDetailResult
import edu.nuce.cinema.ui.library_detail.LibraryDetailViewState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

@FragmentScoped
class LibraryDetailViewModel @ViewModelInject constructor(
    private val actionLibraryDetailProcessor: LibraryDetailActionProcessor
) : ViewModel(), MviViewModel<LibraryDetailIntent, LibraryDetailViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<LibraryDetailIntent> = PublishSubject.create()
    private val statesObservable: Observable<LibraryDetailViewState> = compose()

    override fun processIntents(intents: Observable<LibraryDetailIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<LibraryDetailViewState> = statesObservable

    private fun compose(): Observable<LibraryDetailViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionLibraryDetailProcessor.actionProcessor)
            .scan(LibraryDetailViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: LibraryDetailIntent): LibraryDetailAction {
        return when (intent) {
            is LibraryDetailIntent.GetAnimeIntent -> LibraryDetailAction.GetAnimeAction
            is LibraryDetailIntent.GetMangaIntent -> LibraryDetailAction.GeMangaAction
            is LibraryDetailIntent.GetAnimeByArchiveIntent -> LibraryDetailAction.GetAnimeByArchiveAction(intent.id)
            is LibraryDetailIntent.GetMangaByArchiveIntent -> LibraryDetailAction.GetMangaByArchiveAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: LibraryDetailViewState, result: LibraryDetailResult ->
            when (result) {
                is LibraryDetailResult.GetAnimeResult -> when (result) {
                    is LibraryDetailResult.GetAnimeResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            animes = result.animes
                        )
                    }
                    is LibraryDetailResult.GetAnimeResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is LibraryDetailResult.GetAnimeResult .InFlight -> previousState.copy(isLoading = true)
                }
                is LibraryDetailResult.GetMangaResult -> when (result) {
                    is LibraryDetailResult.GetMangaResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            mangas = result.mangas
                        )
                    }
                    is LibraryDetailResult.GetMangaResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is LibraryDetailResult.GetMangaResult .InFlight -> previousState.copy(isLoading = true)
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