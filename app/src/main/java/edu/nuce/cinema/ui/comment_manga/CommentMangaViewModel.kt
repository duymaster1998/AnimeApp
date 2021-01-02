package edu.nuce.cinema.ui.comment_manga

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import edu.nuce.base.mvi.MviViewModel
import edu.nuce.cinema.ui.anime.AnimeAction
import edu.nuce.cinema.ui.anime.AnimeIntent
import edu.nuce.cinema.ui.anime.AnimeResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

@FragmentScoped
class CommentMangaViewModel @ViewModelInject constructor(
    private val actionCommentMangaActionProcessor: CommentMangaActionProcessor
) : ViewModel(), MviViewModel<CommentMangaIntent, CommentMangaViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<CommentMangaIntent> = PublishSubject.create()
    private val statesObservable: Observable<CommentMangaViewState> = compose()

    override fun processIntents(intents: Observable<CommentMangaIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<CommentMangaViewState> = statesObservable

    private fun compose(): Observable<CommentMangaViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionCommentMangaActionProcessor.actionProcessor)
            .scan(CommentMangaViewState.initialState(), CommentMangaViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: CommentMangaIntent): CommentMangaAction {
        return when (intent) {
            is CommentMangaIntent.GetCommentIntent -> CommentMangaAction.GetCommentAction(id = intent.id)
            is CommentMangaIntent.GetSeriesIntent -> CommentMangaAction.GetSeriesAction(id = intent.id)
            is CommentMangaIntent.CommentIntent -> CommentMangaAction.CommentAction(
                commentParams = intent.commentParams,
                intent.id
            )
            is CommentMangaIntent.GetLikeMeIntent -> CommentMangaAction.GetLikeAction
            is CommentMangaIntent.LikeIntent -> CommentMangaAction.LikeAction(intent.id)
        }
    }

    companion object {
        private val reducer =
            BiFunction { previousState: CommentMangaViewState, result: CommentMangaResult ->
                when (result) {
                    is CommentMangaResult.GetCommentResult -> when (result) {
                        is CommentMangaResult.GetCommentResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                comments = result.comments
                            )
                        }
                        is CommentMangaResult.GetCommentResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is CommentMangaResult.GetCommentResult.InFlight -> previousState.copy(
                            isLoading = true
                        )
                    }
                    is CommentMangaResult.GetSeriesResult -> when (result) {
                        is CommentMangaResult.GetSeriesResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                series = result.series
                            )
                        }
                        is CommentMangaResult.GetSeriesResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is CommentMangaResult.GetSeriesResult.InFlight -> previousState.copy(isLoading = true)
                    }
                    is CommentMangaResult.LikeMeResult -> when (result) {
                        is CommentMangaResult.LikeMeResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                likeMe = result.likes
                            )
                        }
                        is CommentMangaResult.LikeMeResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is CommentMangaResult.LikeMeResult.InFlight -> previousState.copy(isLoading = true)
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