package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

sealed class HomeResult : MviResult {

    sealed class GetAllSeriesResult : HomeResult() {
        data class Success(val allSeries: List<Series>) : GetAllSeriesResult()
        data class Failure(val error: Throwable) : GetAllSeriesResult()
        object InFlight : GetAllSeriesResult()
    }

    sealed class GetTopAnimeResult : HomeResult() {
        data class Success(val topAnime: List<Series>) : GetTopAnimeResult()
        data class Failure(val error: Throwable) : GetTopAnimeResult()
        object InFlight : GetTopAnimeResult()
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
}