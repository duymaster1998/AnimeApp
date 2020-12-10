package edu.nuce.cinema.ui.login

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.User

data class LoginViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val user: User?,
    val oAuthCredentials: DefaultOAuthCredentials?
):MviViewState{
    companion object {
        fun initialState(): LoginViewState {
            return LoginViewState(
                isLoading = false,
                error = null,
                user = null,
                oAuthCredentials = null
            )
        }
    }
}