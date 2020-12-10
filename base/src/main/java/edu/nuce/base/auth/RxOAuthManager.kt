package edu.nuce.base.auth

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * RxOAuthManager provides wrapping for RxJava2 streams, which automatically handles access token
 * expiration and performs refresh token logic defined with [refreshTokenAction], provided by user.
 * In case of success, new credentials are stored in [OAuthStore].
 *
 * The user may provide fallback for refresh token expiration in [onRefreshTokenFailed].
 *
 * The user may provide custom [ErrorChecker] containing access and refresh token expiration
 * checking logic. Otherwise, [DefaultErrorChecker] is applied.
 */
class RxOAuthManager internal constructor(
    private val oAuthStore: OAuthStore,
    private val refreshTokenAction: (String) -> Single<OAuthCredentials>,
    private val onRefreshTokenFailed: (Throwable) -> Unit = {},
    private val errorChecker: ErrorChecker = DefaultErrorChecker()
) {

    constructor(
        sp: SharedPreferences,
        refreshTokenAction: (String) -> Single<OAuthCredentials>,
        onRefreshTokenFailed: (Throwable) -> Unit = {},
        errorChecker: ErrorChecker = DefaultErrorChecker()
    ) : this(OAuthStore(sp), refreshTokenAction, onRefreshTokenFailed, errorChecker)

    constructor(
        context: Context,
        refreshTokenAction: (String) -> Single<OAuthCredentials>,
        onRefreshTokenFailed: (Throwable) -> Unit = {},
        errorChecker: ErrorChecker = DefaultErrorChecker()
    ) : this(OAuthStore(context), refreshTokenAction, onRefreshTokenFailed, errorChecker)

    init {
        initRefreshTokenObservable()
    }

    val accessToken get() = oAuthStore.accessToken

    val refreshToken get() = oAuthStore.refreshToken

    val isExistsCredentials get() = oAuthStore.accessToken != null && oAuthStore.refreshToken != null && oAuthStore.expiresAt != null

    val credentials: OAuthCredentials
        get() = DefaultOAuthCredentials(
            oAuthStore.accessToken!!,
            oAuthStore.refreshToken!!,
            oAuthStore.expiresAt!!
        )

    private var refreshTokenObservable: Observable<OAuthCredentials>? = null

    private fun initRefreshTokenObservable() {
        refreshTokenObservable = Single.defer { refreshAccessToken() }
            .toObservable()
            .share()
            .doOnComplete { initRefreshTokenObservable() }
    }

    fun saveCredentials(credentials: OAuthCredentials) {
        oAuthStore.saveCredentials(credentials)
    }

    fun clearCredentials() {
        oAuthStore.clearCredentials()
    }

    fun provideAuthInterceptor() = OAuthInterceptor(oAuthStore)

    private fun refreshAccessToken(): Single<OAuthCredentials> {
        val refreshToken = oAuthStore.refreshToken ?: ""
        return refreshTokenAction(refreshToken)
            .doOnSuccess { credentials -> saveCredentials(credentials) }
            .doOnError { throwable ->
                if (errorChecker.invalidRefreshToken(throwable)) {
                    clearCredentials()
                    onRefreshTokenFailed(throwable)
                }
            }
    }
}