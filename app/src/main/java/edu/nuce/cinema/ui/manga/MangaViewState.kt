package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.Series

data class MangaViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val manga: Manga?,
    val series: Series?,
    val archives:List<Int>,
) : MviViewState {
    companion object {
        fun initialState(): MangaViewState {
            return MangaViewState(
                isLoading = false,
                error = null,
                manga = null,
                series = null,
                archives = listOf(),
            )
        }
    }
}