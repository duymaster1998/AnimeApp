package edu.nuce.cinema.ui.detail.fragments.season_manga

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import edu.nuce.base.mvi.MviViewModel
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeAction
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeIntent
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

@FragmentScoped
class SeasonMangaViewModel @ViewModelInject constructor(
    private val actionSeasonMangaActionProcessor: SeasonMangaActionProcessor
) : ViewModel(), MviViewModel<SeasonMangaIntent, SeasonMangaViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<SeasonMangaIntent> = PublishSubject.create()
    private val statesObservable: Observable<SeasonMangaViewState> = compose()

    override fun processIntents(intents: Observable<SeasonMangaIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<SeasonMangaViewState> = statesObservable

    private fun compose(): Observable<SeasonMangaViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionSeasonMangaActionProcessor.actionProcessor)
            .scan(SeasonMangaViewState.initialState(), SeasonMangaViewModel.reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: SeasonMangaIntent): SeasonMangaAction {
        return when (intent) {
            is SeasonMangaIntent.GetSeasonsIntent -> SeasonMangaAction.GetSeasonsAction(id = intent.id)
            is SeasonMangaIntent.GetTopCommentIntent -> SeasonMangaAction.GetTopCommentAction(id = intent.id)
            is SeasonMangaIntent.GetLikeMeIntent -> SeasonMangaAction.GetLikeAction
            is SeasonMangaIntent.LikeIntent -> SeasonMangaAction.LikeAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: SeasonMangaViewState, result: SeasonMangaResult ->
            when (result) {
                is SeasonMangaResult.GetSeasonsResult -> when (result) {
                    is SeasonMangaResult.GetSeasonsResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            seasons = result.seasons
                        )
                    }
                    is SeasonMangaResult.GetSeasonsResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SeasonMangaResult.GetSeasonsResult.InFlight -> previousState.copy(
                        isLoading = true
                    )
                }
                is SeasonMangaResult.GetTopCommentResult -> when (result) {
                    is SeasonMangaResult.GetTopCommentResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            comments = result.comments
                        )
                    }
                    is SeasonMangaResult.GetTopCommentResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SeasonMangaResult.GetTopCommentResult.InFlight -> previousState.copy(isLoading = true)
                }
                is SeasonMangaResult.LikeMeResult -> when (result) {
                    is SeasonMangaResult.LikeMeResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            likeMe = result.likes
                        )
                    }
                    is SeasonMangaResult.LikeMeResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is SeasonMangaResult.LikeMeResult.InFlight -> previousState.copy(isLoading = true)
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