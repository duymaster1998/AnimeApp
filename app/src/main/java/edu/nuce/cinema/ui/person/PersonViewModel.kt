package edu.nuce.cinema.ui.person

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
class PersonViewModel @ViewModelInject constructor(
    private val _PersonActionProcessor:PersonActionProcessor
): ViewModel(),MviViewModel<PersonIntent,PersonViewState>{

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<PersonIntent> = PublishSubject.create()
    private val statesObservable: Observable<PersonViewState> = compose()

    override fun processIntents(intents: Observable<PersonIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<PersonViewState> = statesObservable

    private fun compose(): Observable<PersonViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(_PersonActionProcessor.actionProcessor)
            .scan(PersonViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: PersonIntent): PersonAction {
        return when (intent) {
            is PersonIntent.GetAchievementIntent -> PersonAction.GetAchievementAction
            is PersonIntent.GetUserIntent -> PersonAction.GetUserAction
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: PersonViewState, result: PersonResult ->
            when (result) {
                is PersonResult.GetUserResult -> when (result) {
                    is PersonResult.GetUserResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            user = result.user
                        )
                    }
                    is PersonResult.GetUserResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is PersonResult.GetUserResult.InFlight -> previousState.copy(isLoading = true)
                }
                is PersonResult.GetAchievementResult -> when (result) {
                    is PersonResult.GetAchievementResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            achievement = result.achievement
                        )
                    }
                    is PersonResult.GetAchievementResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is PersonResult.GetAchievementResult.InFlight -> previousState.copy(isLoading = true)
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