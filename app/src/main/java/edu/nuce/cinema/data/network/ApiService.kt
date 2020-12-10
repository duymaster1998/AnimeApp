package edu.nuce.cinema.data.network

import androidx.lifecycle.LiveData
import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.cinema.data.models.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import javax.annotation.PostConstruct
import javax.annotation.Signed

interface ApiService {
    @POST("users/login/social")
    fun loginWithSocial(@Body login: Login): Single<DefaultOAuthCredentials>
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
    @GET("series")
    fun getNewSeries(): Observable<List<Series>>
    @GET("series/a/top")
    fun getTopAnime(): Observable<List<Series>>
    @GET("series/m/top")
    fun getTopManga(): Observable<List<Series>>
    @GET("series/{id}")
    fun getSeriesById(@Path("id") id: Int): Single<Series>
    @GET("series/recommend/{id}")
    fun getRecommendSeries(@Path("id") id: Int): Observable<List<Series>>

    //Season
    @GET("seasons/anime/{id}")
    fun getSeasonAnime(@Path("id") id: Int): Observable<List<Season>>
    @GET("seasons/manga/{id}")
    fun getSeasonManga(@Path("id") id: Int): Observable<List<Season>>

    //evaluates
    @GET("evaluates/{seriesId}")
    fun getRateSeries(@Path("seriesId")seriesId:Int): Single<Float>
    @GET("evaluates/me/{seriesId}")
    fun getRateMe(@Path("seriesId")seriesId:Int): Single<Int>
    @POST("evaluates/create")
    fun ratingSeries(@Body() rating: Rating
    ): Single<String>

    //Category
    @GET("categories/top")
    fun getTopCategories(): Observable<List<Category>>

    @GET("series/categories/{seriesId}")
    fun getCategoryBySeries(@Path("seriesId")seriesId:Int): Observable<List<Category>>

    //Anime
    @GET("animes/new")
    fun getNewAnime(): Observable<List<Anime>>
    @GET("animes/season/{id}")
    fun getAnimeBySeason(@Path("id")id:Int): Observable<List<Anime>>
    @POST("animes/update")
    @FormUrlEncoded
    fun updateViewAnime(@Field("id") id: Int):Single<Anime>

    //Manga
    @GET("mangas/new")
    fun getNewManga(): Observable<List<Manga>>
    @GET("mangas/season/{id}")
    fun getMangaBySeason(@Path("id")id:Int): Observable<List<Manga>>
    @POST("mangas/update")
    @FormUrlEncoded
    fun updateViewManga(@Field("id") id: Int):Single<Manga>
    //Comment
    @GET("comments/top/{id}")
    fun getTopCommentAnime(@Path("id") id: Int): Observable<List<Comment>>
    @GET("comments/top/manga/{id}")
    fun getTopCommentManga(@Path("id") id: Int): Observable<List<Comment>>
    @GET("comments/anime/{id}")
    fun getCommentAnime(@Path("id") id: Int): Observable<List<Comment>>

    //Achievement
    @GET("achievements/user")
    fun getAchievements(): Single<Achievement>
    //follow
    @GET("follows/{id}")
    fun isFollow(@Path("id") id: Int): Single<Boolean>
    @POST("follows/create")
    @FormUrlEncoded
    fun createFollow(@Field("seriesId") seriesId:Int):Single<String>

    //author
    @GET("authors/{id}")
    fun getAuhorBySeries(@Path("id") id: Int): Observable<List<Author>>
}