package edu.nuce.cinema.ui.login

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Login

sealed class LoginAction : MviAction{
//    object GetUsersAction : LoginAction()
    object GetUserAction : LoginAction()
    data class LoginWithGoogleAction(val login:Login):LoginAction()
}