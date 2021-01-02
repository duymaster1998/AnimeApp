package edu.nuce.cinema.ui.anime.dialog

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Archive
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

sealed class SaveResult : MviResult {

    sealed class GetSaveListResult : SaveResult() {
        data class Success(val archives: List<Archive>) : GetSaveListResult()
        data class Failure(val error: Throwable) : GetSaveListResult()
        object InFlight : GetSaveListResult()
    }
    sealed class StorageResult : SaveResult() {
        data class Success(val message: String) : StorageResult()
        data class Failure(val error: Throwable) : StorageResult()
        object InFlight : StorageResult()
    }
}