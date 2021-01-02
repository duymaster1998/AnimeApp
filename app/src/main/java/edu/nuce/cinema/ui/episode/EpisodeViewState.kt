package edu.nuce.cinema.ui.episode

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

data class EpisodeViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val animes: List<Anime>,
    val anime: Anime?,
) : MviViewState {

    companion object {
        fun initialState(): EpisodeViewState {
            return EpisodeViewState(
                isLoading = false,
                error = null,
                animes = listOf(),
                anime = null
            )
        }
    }

}