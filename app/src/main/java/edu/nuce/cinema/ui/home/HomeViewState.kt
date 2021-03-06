package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

data class HomeViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val series: Series?,
    val animeN:List<Anime>,
    val allSeries: List<Series>,
    val topAnime: List<Series>,
    val categories:List<Category>
) : MviViewState {

    companion object {
        fun initialState(): HomeViewState {
            return HomeViewState(
                isLoading = false,
                error = null,
                series = null,
                animeN = listOf(),
                allSeries = listOf(),
                topAnime = listOf(),
                categories = listOf()
            )
        }
    }

}