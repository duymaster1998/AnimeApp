package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.Series

sealed class HomeResult : MviResult {

    sealed class GetNewSeriesResult : HomeResult() {
        data class Success(val newSeries: List<Series>) : GetNewSeriesResult()
        data class Failure(val error: Throwable) : GetNewSeriesResult()
        object InFlight : GetNewSeriesResult()
    }

    sealed class GetTopAnimeResult : HomeResult() {
        data class Success(val topAnime: List<Series>) : GetTopAnimeResult()
        data class Failure(val error: Throwable) : GetTopAnimeResult()
        object InFlight : GetTopAnimeResult()
    }

    sealed class GetTopMangaResult : HomeResult() {
        data class Success(val topManga: List<Series>) : GetTopMangaResult()
        data class Failure(val error: Throwable) : GetTopMangaResult()
        object InFlight : GetTopMangaResult()
    }

    sealed class GetTopCategoryResult : HomeResult() {
        data class Success(val categories: List<Category>) : GetTopCategoryResult()
        data class Failure(val error: Throwable) : GetTopCategoryResult()
        object InFlight : GetTopCategoryResult()
    }
    sealed class GetNewAnimeResult : HomeResult() {
        data class Success(val animes: List<Anime>) : GetNewAnimeResult()
        data class Failure(val error: Throwable) : GetNewAnimeResult()
        object InFlight : GetNewAnimeResult()
    }
    sealed class GetNewMangaResult : HomeResult() {
        data class Success(val mangas: List<Manga>) : GetNewMangaResult()
        data class Failure(val error: Throwable) : GetNewMangaResult()
        object InFlight : GetNewMangaResult()
    }
}