package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.Series

data class HomeViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val animeN:List<Anime>,
    val mangaN:List<Manga>,
    val newSeries: List<Series>,
    val topAnime: List<Series>,
    val topManga: List<Series>,
    val categories:List<Category>
) : MviViewState {

    companion object {
        fun initialState(): HomeViewState {
            return HomeViewState(
                isLoading = false,
                error = null,
                animeN = listOf(),
                mangaN = listOf(),
                newSeries = listOf(),
                topAnime = listOf(),
                topManga = listOf(),
                categories = listOf()
            )
        }
    }

}