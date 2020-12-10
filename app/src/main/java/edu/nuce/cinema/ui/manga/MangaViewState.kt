package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.ui.home.HomeViewState

data class MangaViewState (
    val isLoading: Boolean,
    val error: Throwable?,
    val categories:List<Category>,
    val rating:Float
):MviViewState{
    companion object {
        fun initialState(): MangaViewState {
            return MangaViewState(
                isLoading = false,
                error = null,
                categories = listOf(),
                rating = 0F
            )
        }
    }
}