package edu.nuce.cinema.ui.anime.dialog

import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Archive
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

data class SaveViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val archives: List<Archive>,
    val mess:String?
) : MviViewState {

    companion object {
        fun initialState(): SaveViewState {
            return SaveViewState(
                isLoading = false,
                error = null,
                archives = listOf(),
                mess = null
            )
        }
    }

}