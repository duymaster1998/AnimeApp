package edu.nuce.base.auth

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor that adds Authorisation header with access token from [OAuthStore] to Http requests.
 */
class OAuthInterceptor internal constructor(private val oAuthStore: OAuthStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        if (!TextUtils.isEmpty(oAuthStore.accessToken)) {
            val accToken = oAuthStore.accessToken
            builder.addHeader("Authorization", "Bearer $accToken")
        }
        return chain.proceed(builder.build())
    }

}