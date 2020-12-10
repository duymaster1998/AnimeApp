package edu.nuce.cinema.ui.login

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Login

sealed class LoginIntent : MviIntent {
    //    object GetUsersIntent : LoginIntent()
    object GetUserIntent : LoginIntent()
    data class LoginWithGoogleIntent(val login: Login) : LoginIntent()
}