package edu.nuce.cinema.ui.chapter

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
class ChapterViewModel @ViewModelInject constructor(
    private val actionChapterProcessor: ChapterActionProcessor
) : ViewModel(), MviViewModel<ChapterIntent, ChapterViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<ChapterIntent> = PublishSubject.create()
    private val statesObservable: Observable<ChapterViewState> = compose()

    override fun processIntents(intents: Observable<ChapterIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<ChapterViewState> = statesObservable

    private fun compose(): Observable<ChapterViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionChapterProcessor.actionProcessor)
            .scan(ChapterViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ChapterIntent): ChapterAction {
        return when (intent) {
            is ChapterIntent.GetChapterMangaIntent -> ChapterAction.GetChapterMangaAction(intent.id)
            is ChapterIntent.UpdateMangaIntent -> ChapterAction.UpdateMangaAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: ChapterViewState, result: ChapterResult ->
            when (result) {
                is ChapterResult.GetChapterMangaResult -> when (result) {
                    is ChapterResult.GetChapterMangaResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            mangas = result.mangas
                        )
                    }
                    is ChapterResult.GetChapterMangaResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is ChapterResult.GetChapterMangaResult .InFlight -> previousState.copy(isLoading = true)
                }
                is ChapterResult.UpdateMangaResult -> when (result) {
                    is ChapterResult.UpdateMangaResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            manga = result.manga
                        )
                    }
                    is ChapterResult.UpdateMangaResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is ChapterResult.UpdateMangaResult .InFlight -> previousState.copy(isLoading = true)
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