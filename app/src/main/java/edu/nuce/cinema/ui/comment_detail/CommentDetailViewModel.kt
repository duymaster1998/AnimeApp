package edu.nuce.cinema.ui.comment_detail

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
class CommentDetailViewModel @ViewModelInject constructor(
    private val actionCommentDetailActionProcessor: CommentDetailActionProcessor
) : ViewModel(), MviViewModel<CommentDetailIntent, CommentDetailViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<CommentDetailIntent> = PublishSubject.create()
    private val statesObservable: Observable<CommentDetailViewState> = compose()

    override fun processIntents(intents: Observable<CommentDetailIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<CommentDetailViewState> = statesObservable

    private fun compose(): Observable<CommentDetailViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionCommentDetailActionProcessor.actionProcessor)
            .scan(CommentDetailViewState.initialState(), CommentDetailViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: CommentDetailIntent): CommentDetailAction {
        return when (intent) {
            is CommentDetailIntent.GetCommentIntent -> CommentDetailAction.GetCommentAction(id = intent.id)
            is CommentDetailIntent.CommentIntent -> CommentDetailAction.CommentAction(commentParams = intent.commentParams,intent.id)
            is CommentDetailIntent.GetLikeMeIntent -> CommentDetailAction.GetLikeAction
            is CommentDetailIntent.LikeIntent -> CommentDetailAction.LikeAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: CommentDetailViewState, result: CommentDetailResult ->
            when (result) {
                is CommentDetailResult.GetCommentResult -> when (result) {
                    is CommentDetailResult.GetCommentResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            comments = result.comments
                        )
                    }
                    is CommentDetailResult.GetCommentResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is CommentDetailResult.GetCommentResult.InFlight -> previousState.copy(isLoading = true)
                }
                is CommentDetailResult.LikeMeResult -> when (result) {
                    is CommentDetailResult.LikeMeResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            likeMe = result.likes
                        )
                    }
                    is CommentDetailResult.LikeMeResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is CommentDetailResult.LikeMeResult.InFlight -> previousState.copy(isLoading = true)
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