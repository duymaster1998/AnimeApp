package edu.nuce.cinema.ui.detail

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Season

data class DetailViewState (
    val isLoading: Boolean,
    val error: Throwable?,
    val categories:List<Category>,
    val rating:Float,
    val isFollow:Boolean,
    var message:String,
    var unfollow:Boolean,
):MviViewState{
    companion object {
        fun initialState(): DetailViewState {
            return DetailViewState(
                isLoading = false,
                error = null,
                categories = listOf(),
                rating = 0F,
                isFollow = false,
                message = "",
                unfollow = true
            )
        }
    }
}