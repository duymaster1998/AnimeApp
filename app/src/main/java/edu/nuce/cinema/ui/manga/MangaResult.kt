package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Category


sealed class MangaResult : MviResult{
    sealed class GetCategoryBySeriesResult: MangaResult() {
        data class Success(val categories: List<Category>) : GetCategoryBySeriesResult()
        data class Failure(val error: Throwable) : GetCategoryBySeriesResult()
        object InFlight : GetCategoryBySeriesResult()
    }
    sealed class GetRateSeriesResult: MangaResult() {
        data class Success(val rate: Float) : GetRateSeriesResult()
        data class Failure(val error: Throwable) : GetRateSeriesResult()
        object InFlight : GetRateSeriesResult()
    }
}