package edu.nuce.cinema.ui.genre

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.*

data class GenreViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val genres: List<Category>
):MviViewState{
    companion object {
        fun initialState(): GenreViewState {
            return GenreViewState(
                isLoading = false,
                error = null,
                genres = listOf(),
            )
        }
    }
}