package edu.nuce.cinema.ui.series

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Achievement
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.data.models.User

data class SeriesViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val series: List<Series>
):MviViewState{
    companion object {
        fun initialState(): SeriesViewState {
            return SeriesViewState(
                isLoading = false,
                error = null,
                series = listOf(),
            )
        }
    }
}