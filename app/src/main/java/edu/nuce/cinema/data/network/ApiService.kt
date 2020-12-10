package edu.nuce.cinema.data.network

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.cinema.data.models.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ApiService {
    @POST("users/login/social")
    fun loginWithSocial(@Body login: Login?): Single<DefaultOAuthCredentials>
    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Single<User>
    @GET("users/me")
    fun getAuthUser(): Single<User>
    @POST("users/refreshToken")
    @FormUrlEncoded
    fun refreshAccessToken(@Field("refreshToken") token: String): Single<OAuthCredentials>

    //Series
    @GET("series")
    fun getSeries(): Observable<List<Series>>
    @GET("series/a/top")
    fun getTopAnime(): Observable<List<Series>>
    @GET("series/anime/{id}")
    fun getSeriesByAnime(@Path("id") id: Int): Single<Series>
    @GET("series/{id}")
    fun getSeriesById(@Path("id") id: Int): Single<Series>

    //
    @GET("evaluates/{seriesId}")
    fun getRateSeries(@Path("seriesId")seriesId:Int): Single<Float>

    //Category
    @GET("categories/top")
    fun getTopCategories(): Observable<List<Category>>
    @GET("series/categories/{seriesId}")
    fun getCategoryBySeries(@Path("seriesId")seriesId:Int): Observable<List<Category>>

    //Anime
    @GET("animes/new")
    fun getNewAnime(): Observable<List<Anime>>
}