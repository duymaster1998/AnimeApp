package edu.nuce.cinema.ui.detail.fragments.description

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Author
import edu.nuce.cinema.data.models.Rating
import edu.nuce.cinema.data.models.Series

data class DescriptionViewState (
    val isLoading: Boolean,
    val error: Throwable?,
    val series:List<Series>,
    val authors:List<Author>,
    val rating:Float,
    val rateMe:Int,
    val rateNow:String
):MviViewState{
    companion object {
        fun initialState(): DescriptionViewState {
            return DescriptionViewState(
                isLoading = false,
                error = null,
                series = listOf(),
                authors = listOf(),
                rating = 0F,
                rateMe = 0,
                rateNow = "",
            )
        }
    }
}