package edu.nuce.cinema.ui.comment_manga

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Series

data class CommentMangaViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val comments: List<Comment>,
    val series: Series?,
    val likeMe:List<Int>
) : MviViewState {
    companion object {
        fun initialState(): CommentMangaViewState {
            return CommentMangaViewState(
                isLoading = false,
                error = null,
                comments = listOf(),
                likeMe = listOf(),
                series = null,
            )
        }
    }
}