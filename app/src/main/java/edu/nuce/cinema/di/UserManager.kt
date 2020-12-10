//package edu.nuce.cinema.di
//
//import edu.nuce.base.auth.OAuthCredentials
//import edu.nuce.base.auth.RxOAuthManager
//import javax.inject.Inject
//import javax.inject.Provider
//import javax.inject.Singleton
//
//@Singleton
//class UserManager @Inject constructor(
//    private val rxOAuthManager: RxOAuthManager,
//    private val userSessionComponent: Provider<UserSessionComponent.Builder>
//) {
//
//    var userComponent: UserSessionComponent? = null
//        private set
//
//    fun isUserLoggedIn(): Boolean = rxOAuthManager.isExistsCredentials && userComponent != null
//
//    fun userLoggedIn(oAuthCredentials: OAuthCredentials? = null) {
//        if (oAuthCredentials == null) {
//            if (rxOAuthManager.isExistsCredentials)
//                userComponent = userSessionComponent.get().setCredentials(rxOAuthManager.credentials).build()
//        } else {
//            rxOAuthManager.saveCredentials(oAuthCredentials)
//            userComponent = userSessionComponent.get().setCredentials(oAuthCredentials).build()
//        }
//    }
//
//    fun logout() {
//        rxOAuthManager.clearCredentials()
//        userComponent = null
//    }
//
//}