package edu.nuce.cinema.ui.login

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
class LoginViewModel @ViewModelInject constructor(
    private val _loginActionProcessor:LoginActionProcessor
): ViewModel(),MviViewModel<LoginIntent,LoginViewState>{

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<LoginIntent> = PublishSubject.create()
    private val statesObservable: Observable<LoginViewState> = compose()

    override fun processIntents(intents: Observable<LoginIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<LoginViewState> = statesObservable

    private fun compose(): Observable<LoginViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(_loginActionProcessor.actionProcessor)
            .scan(LoginViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: LoginIntent): LoginAction {
        return when (intent) {
            is LoginIntent.LoginWithGoogleIntent -> LoginAction.LoginWithGoogleAction(login = intent.login)
            is LoginIntent.GetUserIntent -> LoginAction.GetUserAction
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: LoginViewState, result: LoginResult ->
            when (result) {
                is LoginResult.GetUserResult -> when (result) {
                    is LoginResult.GetUserResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            user = result.user
                        )
                    }
                    is LoginResult.GetUserResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is LoginResult.GetUserResult.InFlight -> previousState.copy(isLoading = true)
                }
                is LoginResult.LoginWithGoogleResult -> when (result) {
                    is LoginResult.LoginWithGoogleResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            defaultOAuthCredentials = result.response
                        )
                    }
                    is LoginResult.LoginWithGoogleResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is LoginResult.LoginWithGoogleResult.InFlight -> previousState.copy(isLoading = true)
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