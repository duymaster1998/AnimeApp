package edu.nuce.cinema.ui.series

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Series

sealed class SeriesResult : MviResult {

    sealed class GetSeriesResult : SeriesResult() {
        data class Success(val series: List<Series>) : GetSeriesResult()
        data class Failure(val error: Throwable) : GetSeriesResult()
        object InFlight : GetSeriesResult()
    }
}