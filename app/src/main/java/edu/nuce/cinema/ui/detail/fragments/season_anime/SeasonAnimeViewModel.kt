package edu.nuce.cinema.ui.detail.fragments.season_anime

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
class SeasonAnimeViewModel @ViewModelInject constructor(
    private val actionSeasonAnimeActionProcessor: SeasonAnimeActionProcessor
) : ViewModel(), MviViewModel<SeasonAnimeIntent, SeasonAnimeViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<SeasonAnimeIntent> = PublishSubject.create()
    private val statesObservable: Observable<SeasonAnimeViewState> = compose()

    override fun processIntents(intents: Observable<SeasonAnimeIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<SeasonAnimeViewState> = statesObservable

    private fun compose(): Observable<SeasonAnimeViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionSeasonAnimeActionProcessor.actionProcessor)
            .scan(SeasonAnimeViewState.initialState(), SeasonAnimeViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: SeasonAnimeIntent): SeasonAnimeAction {
        return when (intent) {
            is SeasonAnimeIntent.GetSeasonsIntent -> SeasonAnimeAction.GetSeasonsAction(id = intent.id)
            is SeasonAnimeIntent.GetTopCommentIntent -> SeasonAnimeAction.GetTopCommentAction(id = intent.id)
            is SeasonAnimeIntent.GetLikeMeIntent -> SeasonAnimeAction.GetLikeAction
            is SeasonAnimeIntent.LikeIntent -> SeasonAnimeAction.LikeAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: SeasonAnimeViewState, result: SeasonAnimeResult ->
            when (result) {
                is SeasonAnimeResult.GetSeasonsResult -> when (result) {
                    is SeasonAnimeResult.GetSeasonsResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            seasons = result.seasons
                        )
                    }
                    is SeasonAnimeResult.GetSeasonsResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SeasonAnimeResult.GetSeasonsResult.InFlight -> previousState.copy(
                        isLoading = true
                    )
                }
                is SeasonAnimeResult.GetTopCommentResult -> when (result) {
                    is SeasonAnimeResult.GetTopCommentResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            comments = result.comments
                        )
                    }
                    is SeasonAnimeResult.GetTopCommentResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SeasonAnimeResult.GetTopCommentResult.InFlight -> previousState.copy(isLoading = true)
                }
                is SeasonAnimeResult.LikeMeResult -> when (result) {
                    is SeasonAnimeResult.LikeMeResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            likeMe = result.likes
                        )
                    }
                    is SeasonAnimeResult.LikeMeResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SeasonAnimeResult.LikeMeResult.InFlight -> previousState.copy(isLoading = true)
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