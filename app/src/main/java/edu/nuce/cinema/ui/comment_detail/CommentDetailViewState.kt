package edu.nuce.cinema.ui.comment_detail

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Series

data class CommentDetailViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val comments: List<Comment>,
    val likeMe:List<Int>
) : MviViewState {
    companion object {
        fun initialState(): CommentDetailViewState {
            return CommentDetailViewState(
                isLoading = false,
                error = null,
                comments = listOf(),
                likeMe = listOf(),
            )
        }
    }
}