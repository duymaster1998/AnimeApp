package edu.nuce.cinema.ui.genre

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

sealed class GenreResult : MviResult {

    sealed class GetGenreResult : GenreResult() {
        data class Success(val genres: List<Category>) : GetGenreResult()
        data class Failure(val error: Throwable) : GetGenreResult()
        object InFlight : GetGenreResult()
    }
}