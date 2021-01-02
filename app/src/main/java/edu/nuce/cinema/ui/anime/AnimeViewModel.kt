package edu.nuce.cinema.ui.anime

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
class AnimeViewModel @ViewModelInject constructor(
    private val actionAnimeActionProcessor: AnimeActionProcessor
) : ViewModel(), MviViewModel<AnimeIntent, AnimeViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<AnimeIntent> = PublishSubject.create()
    private val statesObservable: Observable<AnimeViewState> = compose()

    override fun processIntents(intents: Observable<AnimeIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<AnimeViewState> = statesObservable

    private fun compose(): Observable<AnimeViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionAnimeActionProcessor.actionProcessor)
            .scan(AnimeViewState.initialState(), AnimeViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: AnimeIntent): AnimeAction {
        return when (intent) {
            is AnimeIntent.UpdateAnimeIntent -> AnimeAction.UpdateAnimeAction(id = intent.id,intent.historyParams)
            is AnimeIntent.GetCommentIntent -> AnimeAction.GetCommentAction(id = intent.id)
            is AnimeIntent.GetSeriesIntent -> AnimeAction.GetSeriesAction(id = intent.id)
            is AnimeIntent.GetArchiveIntent -> AnimeAction.GetArchiveAction(id = intent.id)
            is AnimeIntent.CommentIntent -> AnimeAction.CommentAction(commentParams = intent.commentParams,id = intent.id)
            is AnimeIntent.GetLikeMeIntent -> AnimeAction.GetLikeAction
            is AnimeIntent.LikeIntent -> AnimeAction.LikeAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: AnimeViewState, result: AnimeResult ->
            when (result) {
                is AnimeResult.UpdateAnimeResult -> when (result) {
                    is AnimeResult.UpdateAnimeResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            anime = result.anime
                        )
                    }
                    is AnimeResult.UpdateAnimeResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.UpdateAnimeResult.InFlight -> previousState.copy(
                        isLoading = true
                    )
                }
                is AnimeResult.GetCommentResult -> when (result) {
                    is AnimeResult.GetCommentResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            comments = result.comments
                        )
                    }
                    is AnimeResult.GetCommentResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.GetCommentResult.InFlight -> previousState.copy(isLoading = true)
                }
                is AnimeResult.GetSeriesResult -> when (result) {
                    is AnimeResult.GetSeriesResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            series = result.series
                        )
                    }
                    is AnimeResult.GetSeriesResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.GetSeriesResult.InFlight -> previousState.copy(isLoading = true)
                }
                is AnimeResult.GetArchiveResult -> when (result) {
                    is AnimeResult.GetArchiveResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            archives = result.archives
                        )
                    }
                    is AnimeResult.GetArchiveResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.GetArchiveResult.InFlight -> previousState.copy(isLoading = true)
                }
                is AnimeResult.CommentResult -> when (result) {
                    is AnimeResult.CommentResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            comments = result.comments
                        )
                    }
                    is AnimeResult.CommentResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.CommentResult.InFlight -> previousState.copy(isLoading = true)
                }
                is AnimeResult.LikeMeResult -> when (result) {
                    is AnimeResult.LikeMeResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            likeMe = result.likes
                        )
                    }
                    is AnimeResult.LikeMeResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is AnimeResult.LikeMeResult.InFlight -> previousState.copy(isLoading = true)
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