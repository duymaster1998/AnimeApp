package edu.nuce.cinema.ui.detail.fragments.season_anime

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Season

data class SeasonAnimeViewState (
    val isLoading: Boolean,
    val error: Throwable?,
    val seasons: List<Season>,
    val comments:List<Comment>,
    val likeMe:List<Int>
):MviViewState{
    companion object {
        fun initialState(): SeasonAnimeViewState {
            return SeasonAnimeViewState(
                isLoading = false,
                error = null,
                seasons = listOf(),
                comments = listOf(),
                likeMe = listOf(),
            )
        }
    }
}