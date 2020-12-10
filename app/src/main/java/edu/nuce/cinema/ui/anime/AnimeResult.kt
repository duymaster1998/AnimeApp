package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.ui.home.HomeResult


sealed class AnimeResult : MviResult{
    sealed class GetCategoryBySeriesResult: AnimeResult() {
        data class Success(val categories: List<Category>) : GetCategoryBySeriesResult()
        data class Failure(val error: Throwable) : GetCategoryBySeriesResult()
        object InFlight : GetCategoryBySeriesResult()
    }
    sealed class GetRateSeriesResult: AnimeResult() {
        data class Success(val rate: Float) : GetRateSeriesResult()
        data class Failure(val error: Throwable) : GetRateSeriesResult()
        object InFlight : GetRateSeriesResult()
    }
    sealed class GetSeriesResult : AnimeResult() {
        data class Success(val series: Series) : GetSeriesResult()
        data class Failure(val error: Throwable) : GetSeriesResult()
        object InFlight : GetSeriesResult()
    }
    sealed class GetSeriesAResult : AnimeResult() {
        data class Success(val series: Series) : GetSeriesAResult()
        data class Failure(val error: Throwable) : GetSeriesAResult()
        object InFlight : GetSeriesAResult()
    }
}