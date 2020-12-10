package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.ui.home.HomeViewState

data class AnimeViewState (
    val isLoading: Boolean,
    val error: Throwable?,
    val categories:List<Category>,
    val rating:Float,
    val series: Series?
):MviViewState{
    companion object {
        fun initialState(): AnimeViewState {
            return AnimeViewState(
                isLoading = false,
                error = null,
                categories = listOf(),
                rating = 0F,
                series = null
            )
        }
    }
}