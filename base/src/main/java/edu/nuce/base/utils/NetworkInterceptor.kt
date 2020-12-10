package edu.nuce.base.utils

import android.content.Context
import com.google.gson.Gson
import edu.nuce.base.extensions.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

class NetworkInterceptor(private val context: Context, private val gson: Gson) : Interceptor {

    private val networkEvent: NetworkEvent = NetworkEvent

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        /*
        * Step 2: We check if there is internet
        * available in the device. If not, pass
        * the networkState as NO_INTERNET.
        * */
        if (!context.isNetworkAvailable()) {
            networkEvent.publish(NetworkState.NoInternet)
        } else {
            try {
                /*
                 * Step 3: Get the response code from the
                 * request. In this scenario we are only handling
                 * unauthorised and server unavailable error
                 * scenario
                 * */
                val response = chain.proceed(request)
                when (response.code) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> networkEvent.publish(NetworkState.Unauthorized)

                    HttpURLConnection.HTTP_UNAVAILABLE -> networkEvent.publish(
                        NetworkState.NoResponse(
                            gson.fromJson(response.body?.string(), ApiException::class.java)
                        )
                    )

                    HttpURLConnection.HTTP_BAD_REQUEST -> networkEvent.publish(
                        NetworkState.BadRequest(
                            gson.fromJson(response.body?.string(), ApiException::class.java)
                        )
                    )

                    HttpURLConnection.HTTP_FORBIDDEN -> networkEvent.publish(
                        NetworkState.BadRequest(
                            gson.fromJson(response.body?.string(), ApiException::class.java)
                        )
                    )
                }
                return response

            } catch (e: SocketTimeoutException) {
                networkEvent.publish(NetworkState.RequestTimeout(
                    ApiException(
                        null,
                        e.localizedMessage,
                        e.message,
                        null,
                        null,
                        null
                    )
                ))
            } catch (e: IOException) {
                networkEvent.publish(
                    NetworkState.NoResponse(
                        ApiException(
                            500,
                            e.localizedMessage,
                            e.message,
                            null,
                            null,
                            null
                        )
                    )
                )
            }
        }
        return chain.proceed(request)
    }

}