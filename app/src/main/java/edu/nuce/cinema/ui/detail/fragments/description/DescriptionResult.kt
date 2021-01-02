package edu.nuce.cinema.ui.detail.fragments.description

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Author
import edu.nuce.cinema.data.models.Rating
import edu.nuce.cinema.data.models.Series


sealed class DescriptionResult : MviResult {
    sealed class GetRecommendResult : DescriptionResult() {
        data class Success(val series: List<Series>) : GetRecommendResult()
        data class Failure(val error: Throwable) : GetRecommendResult()
        object InFlight : GetRecommendResult()
    }

    sealed class GetRateSeriesResult : DescriptionResult() {
        data class Success(val rate: Float) : GetRateSeriesResult()
        data class Failure(val error: Throwable) : GetRateSeriesResult()
        object InFlight : GetRateSeriesResult()
    }
    sealed class GetAuthorResult : DescriptionResult() {
        data class Success(val authors: List<Author>) : GetAuthorResult()
        data class Failure(val error: Throwable) : GetAuthorResult()
        object InFlight : GetAuthorResult()
    }
    sealed class GetRateMeResult : DescriptionResult() {
        data class Success(val rateMe: Int) : GetRateMeResult()
        data class Failure(val error: Throwable) : GetRateMeResult()
        object InFlight : GetRateMeResult()
    }
}