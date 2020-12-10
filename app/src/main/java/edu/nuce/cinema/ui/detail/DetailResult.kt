package edu.nuce.cinema.ui.detail

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Season


sealed class DetailResult : MviResult{
    sealed class GetCategoryBySeriesResult: DetailResult() {
        data class Success(val categories: List<Category>) : GetCategoryBySeriesResult()
        data class Failure(val error: Throwable) : GetCategoryBySeriesResult()
        object InFlight : GetCategoryBySeriesResult()
    }
    sealed class GetRateSeriesResult: DetailResult() {
        data class Success(val rate: Float) : GetRateSeriesResult()
        data class Failure(val error: Throwable) : GetRateSeriesResult()
        object InFlight : GetRateSeriesResult()
    }
    sealed class IsFollowResult: DetailResult() {
        data class Success(val isFollow: Boolean) : IsFollowResult()
        data class Failure(val error: Throwable) : IsFollowResult()
        object InFlight : IsFollowResult()
    }
    sealed class FollowResult: DetailResult() {
        data class Success(val message: String) : FollowResult()
        data class Failure(val error: Throwable) : FollowResult()
        object InFlight : FollowResult()
    }
}