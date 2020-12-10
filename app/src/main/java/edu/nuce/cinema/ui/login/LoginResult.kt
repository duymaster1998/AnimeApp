package edu.nuce.cinema.ui.login

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.User

sealed class LoginResult : MviResult{
    sealed class LoginWithGoogleResult:LoginResult(){
        data class Success(val response: DefaultOAuthCredentials) : LoginWithGoogleResult()
        data class Failure(val error: Throwable) : LoginWithGoogleResult()
        object InFlight : LoginWithGoogleResult()
    }
    sealed class GetUserResult: LoginResult() {
        data class Success(val user: User) : GetUserResult()
        data class Failure(val error: Throwable) : GetUserResult()
        object InFlight : GetUserResult()
    }
}