package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Series

data class AnimeViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val anime: Anime?,
    val comments: List<Comment>,
    val comment: Comment?,
    val series: Series?,
    val archives:List<Int>,
    val likeMe:List<Int>
) : MviViewState {
    companion object {
        fun initialState(): AnimeViewState {
            return AnimeViewState(
                isLoading = false,
                error = null,
                anime = null,
                comments = listOf(),
                comment = null,
                series = null,
                archives = listOf(),
                likeMe = listOf()
            )
        }
    }
}