package edu.nuce.cinema.di

import android.content.Context
import androidx.lifecycle.map
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.franmontiel.persistentcookiejar.BuildConfig
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.nuce.base.auth.OAuthInterceptor
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.utils.NetworkInterceptor
import edu.nuce.cinema.R
import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.di.auth.IAuthDelegate
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    private const val BASE_URL = "http://192.168.1.37:8080/api/"
    @Singleton
    @Provides
    fun provideRxOAuthManager(
        @ApplicationContext context: Context,
        apiService: Lazy<ApiService>
    ): RxOAuthManager {
        return RxOAuthManager(
            context = context,
            refreshTokenAction = { apiService.get()!!.refreshAccessToken(it).map { it } }
        )
    }

    @Singleton
    @Provides
    fun providerAuthDelegate(rxOAuthManager: RxOAuthManager):IAuthDelegate =
        IAuthDelegate(rxOAuthManager)


    @Singleton
    @Provides
    fun provideGsonInstance(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideNetworkInterceptor(@ApplicationContext context: Context, gson: Gson): NetworkInterceptor =
        NetworkInterceptor(context, gson)

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpInstance(
        @ApplicationContext context: Context,
        interceptor: Interceptor,
        apiInterceptor: NetworkInterceptor,
        rxOAuthManager: RxOAuthManager
    ): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
        return OkHttpClient.Builder()
            .connectTimeout(30, SECONDS)
            .writeTimeout(30, SECONDS)
            .readTimeout(30, SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addNetworkInterceptor(interceptor)
            .addNetworkInterceptor(rxOAuthManager.provideAuthInterceptor())
            .cookieJar(cookieJar)
            .addInterceptor(apiInterceptor)
            .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptorInstance(): Interceptor {
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.e("\n$message")
            }
        })
        logging.setLevel(if (BuildConfig.DEBUG) BODY else NONE)
        return logging
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRequestManager(
        @ApplicationContext context: Context,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
    }

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {   //return truoc khi load dk anh
        return RequestOptions()
            .placeholder(R.drawable.shadow_bottom_to_top)
            .error(R.drawable.shadow_bottom_to_top)
    }
}