package edu.nuce.cinema.ui.library

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Archive
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

data class LibraryViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val series: List<Series>,
    val archives: List<Archive>,
) : MviViewState {

    companion object {
        fun initialState(): LibraryViewState {
            return LibraryViewState(
                isLoading = false,
                error = null,
                series = listOf(),
                archives = listOf()
            )
        }
    }

}