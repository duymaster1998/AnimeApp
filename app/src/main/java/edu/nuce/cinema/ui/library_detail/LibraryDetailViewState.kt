package edu.nuce.cinema.ui.library_detail

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.*

data class LibraryDetailViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val animes: List<Anime>,
    val mangas: List<Manga>,
) : MviViewState {

    companion object {
        fun initialState(): LibraryDetailViewState {
            return LibraryDetailViewState(
                isLoading = false,
                error = null,
                animes = listOf(),
                mangas = listOf()
            )
        }
    }

}