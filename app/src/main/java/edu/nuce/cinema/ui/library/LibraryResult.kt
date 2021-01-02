package edu.nuce.cinema.ui.library

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Archive
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

sealed class LibraryResult : MviResult {

    sealed class GetSubscribeSeriesResult : LibraryResult() {
        data class Success(val series: List<Series>) : GetSubscribeSeriesResult()
        data class Failure(val error: Throwable) : GetSubscribeSeriesResult()
        object InFlight : GetSubscribeSeriesResult()
    }
    sealed class GetSaveListResult : LibraryResult() {
        data class Success(val archives: List<Archive>) : GetSaveListResult()
        data class Failure(val error: Throwable) : GetSaveListResult()
        object InFlight : GetSaveListResult()
    }
}