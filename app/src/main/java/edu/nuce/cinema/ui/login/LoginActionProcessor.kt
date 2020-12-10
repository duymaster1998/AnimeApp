package edu.nuce.cinema.ui.login

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginActionProcessor @Inject constructor(
    private val _apiService: ApiService,
//    private val rxOAuthManager: RxOAuthManager
) {
    private val getUserProcessor =
        ObservableTransformer<LoginAction.GetUserAction, LoginResult.GetUserResult> { action ->
            action.flatMap { userAction ->
                _apiService.getAuthUser()
                    .toObservable()
                    .map { LoginResult.GetUserResult.Success(user = it) }
                    .cast(LoginResult.GetUserResult::class.java)
                    .onErrorReturn(LoginResult.GetUserResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LoginResult.GetUserResult.InFlight)
            }
        }
    private val loginWithGoogleActionProcessor =
        ObservableTransformer<LoginAction.LoginWithGoogleAction,LoginResult.LoginWithGoogleResult>{ action->
            action.flatMap {
                _apiService.loginWithSocial(it.login)
                    .toObservable()
                    .map { LoginResult.LoginWithGoogleResult.Success(response = it) }
                    .cast(LoginResult.LoginWithGoogleResult::class.java)
                    .onErrorReturn(LoginResult.LoginWithGoogleResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LoginResult.LoginWithGoogleResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<LoginAction, LoginResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(LoginAction.LoginWithGoogleAction::class.java).compose(loginWithGoogleActionProcessor),
                shared.ofType(LoginAction.GetUserAction::class.java).compose(getUserProcessor)
            )
        }
    }
}