package edu.nuce.cinema.data.network

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.cinema.data.models.*
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.data.models.request.HistoryParams
import edu.nuce.cinema.data.models.request.StorageParams
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

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

    @GET("series/anime/{id}")
    fun getSeriesByAnime(@Path("id") id: Int): Single<Series>

    @GET("series/manga/{id}")
    fun getSeriesByManga(@Path("id") id: Int): Single<Series>

    @GET("categories/series/{id}")
    fun getSeriesByCategory(@Path("id") id: Int): Observable<List<Series>>

    @GET("series/recommend/{id}")
    fun getRecommendSeries(@Path("id") id: Int): Observable<List<Series>>

    @GET("series/subscribe")
    fun getSubscribeSeries(): Observable<List<Series>>

    @POST("series/like")
    @FormUrlEncoded
    fun getSeriesLike(@Field("name") input: String): Observable<List<Series>>

    @POST("categories/series/like")
    @FormUrlEncoded
    fun getSeriesByCategoryLike(
        @Field("name") input: String,
        @Field("id") id: Int
    ): Observable<List<Series>>

    //Season
    @GET("seasons/anime/{id}")
    fun getSeasonAnime(@Path("id") id: Int): Observable<List<Season>>

    @GET("seasons/manga/{id}")
    fun getSeasonManga(@Path("id") id: Int): Observable<List<Season>>

    //evaluates
    @GET("evaluates/{seriesId}")
    fun getRateSeries(@Path("seriesId") seriesId: Int): Single<Float>

    @GET("evaluates/me/{seriesId}")
    fun getRateMe(@Path("seriesId") seriesId: Int): Single<Int>

    @POST("evaluates/create")
    fun ratingSeries(
        @Body() rating: Rating
    ): Completable

    //Category
    @GET("categories/top")
    fun getTopCategories(): Observable<List<Category>>

    @GET("categories")
    fun getCategories(): Observable<List<Category>>

    @POST("categories/like")
    @FormUrlEncoded
    fun getCategoriesLike(@Field("name") name: String): Observable<List<Category>>


    @GET("series/categories/{seriesId}")
    fun getCategoryBySeries(@Path("seriesId") seriesId: Int): Observable<List<Category>>

    //Anime
    @GET("animes/new")
    fun getNewAnime(): Observable<List<Anime>>

    @GET("animes/season/{id}")
    fun getAnimeBySeason(@Path("id") id: Int): Observable<List<Anime>>

    @POST("animes/update")
    @FormUrlEncoded
    fun updateViewAnime(@Field("id") id: Int): Single<Anime>

    //Manga
    @GET("mangas/new")
    fun getNewManga(): Observable<List<Manga>>

    @GET("mangas/season/{id}")
    fun getMangaBySeason(@Path("id") id: Int): Observable<List<Manga>>

    @POST("mangas/update")
    @FormUrlEncoded
    fun updateViewManga(@Field("id") id: Int): Single<Manga>

    //Comment
    @GET("comments/top/{id}")
    fun getTopCommentAnime(@Path("id") id: Int): Observable<List<Comment>>

    @GET("comments/parent/{id}")
    fun getChildCommentAnime(@Path("id") id: Int): Observable<List<Comment>>

    @GET("comments/top/manga/{id}")
    fun getTopCommentManga(@Path("id") id: Int): Observable<List<Comment>>

    @GET("comments/anime/{id}")
    fun getCommentAnime(@Path("id") id: Int): Observable<List<Comment>>

    @GET("comments/manga/{id}")
    fun getCommentManga(@Path("id") id: Int): Observable<List<Comment>>

    @POST("comments/create")
    fun commentSeries(
        @Body() commentParams: CommentParams
    ): Completable

    //Achievement
    @GET("achievements/user")
    fun getAchievements(): Single<Achievement>

    //follow
    @GET("follows/{id}")
    fun isFollow(@Path("id") id: Int): Single<Boolean>

    @DELETE("follows/{id}")
    fun unFollow(@Path("id") id: Int): Completable

    @POST("follows/create")
    @FormUrlEncoded
    fun createFollow(@Field("seriesId") seriesId: Int): Completable

    //author
    @GET("authors/{id}")
    fun getAuhorBySeries(@Path("id") id: Int): Observable<List<Author>>

    //archive
    @GET("archives")
    fun getArchiveByUser(): Observable<List<Archive>>

    @GET("archives/anime/{id}")
    fun getArchiveOfAnime(@Path("id") id: Int): Observable<List<Int>>

    @GET("archives/manga/{id}")
    fun getArchiveOfManga(@Path("id") id: Int): Observable<List<Int>>

    @POST("archives/create")
    @FormUrlEncoded
    fun addArchive(
        @Field("name") name: String
    ): Completable
    @DELETE("archives/{id}")
    fun removeArchive(@Path("id") id: Int): Completable

    //History
    @POST("histories/create")
    fun historySeries(
        @Body() historyParams: HistoryParams
    ): Completable

    @GET("histories/anime")
    fun getAnimeHistories(): Observable<List<Anime>>

    @GET("histories/manga")
    fun getMangaHistories(): Observable<List<Manga>>

    //storage
    @GET("storages/anime/{archiveId}")
    fun getAnimeByArchive(@Path("archiveId") archiveId: Int): Observable<List<Anime>>

    @GET("storages/manga/{archiveId}")
    fun getMangaByArchive(@Path("archiveId") archiveId: Int): Observable<List<Manga>>

    @POST("storages/create")
    fun addItemIntoStorage(@Body() storageParams: StorageParams): Single<String>

    //like
    @GET("likes/me")
    fun getLikeMe(): Observable<List<Int>>

    @POST("likes/like")
    @FormUrlEncoded
    fun LikeComment(@Field("commentId") id: Int): Completable
}