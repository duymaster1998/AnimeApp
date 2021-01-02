package edu.nuce.cinema.ui.chapter

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.Series

data class ChapterViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val mangas: List<Manga>,
    val manga: Manga?,
) : MviViewState {

    companion object {
        fun initialState(): ChapterViewState {
            return ChapterViewState(
                isLoading = false,
                error = null,
                mangas = listOf(),
                manga = null
            )
        }
    }

}