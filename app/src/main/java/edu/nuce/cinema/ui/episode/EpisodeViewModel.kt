package edu.nuce.cinema.ui.episode

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
class EpisodeViewModel @ViewModelInject constructor(
    private val actionEpisodeProcessor: EpisodeActionProcessor
) : ViewModel(), MviViewModel<EpisodeIntent, EpisodeViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<EpisodeIntent> = PublishSubject.create()
    private val statesObservable: Observable<EpisodeViewState> = compose()

    override fun processIntents(intents: Observable<EpisodeIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<EpisodeViewState> = statesObservable

    private fun compose(): Observable<EpisodeViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(actionEpisodeProcessor.actionProcessor)
            .scan(EpisodeViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: EpisodeIntent): EpisodeAction {
        return when (intent) {
            is EpisodeIntent.GetEpisodeAnimeIntent -> EpisodeAction.GetEpisodeAnimeAction(intent.id)
            is EpisodeIntent.UpdateAnimeIntent -> EpisodeAction.UpdateAnimeAction(intent.id)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: EpisodeViewState, result: EpisodeResult ->
            when (result) {
                is EpisodeResult.GetEpisodeAnimeResult -> when (result) {
                    is EpisodeResult.GetEpisodeAnimeResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            animes = result.animes
                        )
                    }
                    is EpisodeResult.GetEpisodeAnimeResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is EpisodeResult.GetEpisodeAnimeResult .InFlight -> previousState.copy(isLoading = true)
                }
                is EpisodeResult.UpdateAnimeResult -> when (result) {
                    is EpisodeResult.UpdateAnimeResult .Success -> {
                        previousState.copy(
                            isLoading = false,
                            anime = result.anime
                        )
                    }
                    is EpisodeResult.UpdateAnimeResult .Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is EpisodeResult.UpdateAnimeResult .InFlight -> previousState.copy(isLoading = true)
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