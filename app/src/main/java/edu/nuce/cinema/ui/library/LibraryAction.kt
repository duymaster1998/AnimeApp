package edu.nuce.cinema.ui.library

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Anime

sealed class LibraryAction : MviAction {
    object GetSubscibeSeriesAction : LibraryAction()
    object GetSaveListAction : LibraryAction()
    data class AddSaveListAction(val name:String) : LibraryAction()
    data class RemoveArchiveAction(val id:Int) : LibraryAction()
}