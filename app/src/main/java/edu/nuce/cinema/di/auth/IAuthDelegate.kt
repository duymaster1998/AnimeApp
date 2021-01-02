package edu.nuce.cinema.di.auth

import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.cinema.data.models.User
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class IAuthDelegate @Inject constructor(
    private val rxOAuthManager: RxOAuthManager,
):AuthViewModelDelegate {
    override fun isSignedIn(): Boolean = rxOAuthManager.isExistsCredentials

    override fun requestSignIn(oAuthCredentials: OAuthCredentials?) {
        if(oAuthCredentials!= null){
            rxOAuthManager.saveCredentials(oAuthCredentials)
        }
    }

    override fun requestLogout() {
        rxOAuthManager.clearCredentials()
    }
}