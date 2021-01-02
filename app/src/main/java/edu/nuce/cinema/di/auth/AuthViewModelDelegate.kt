package edu.nuce.cinema.di.auth

import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.cinema.data.models.User
import io.reactivex.rxjava3.core.Single

interface AuthViewModelDelegate {
    fun isSignedIn():Boolean
    fun requestSignIn(oAuthCredentials: OAuthCredentials? = null)
    fun requestLogout()
}