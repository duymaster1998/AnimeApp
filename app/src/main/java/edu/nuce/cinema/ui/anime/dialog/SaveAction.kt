package edu.nuce.cinema.ui.anime.dialog

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.data.models.request.HistoryParams
import edu.nuce.cinema.data.models.request.StorageParams
import edu.nuce.cinema.ui.library.LibraryAction

sealed class SaveAction:MviAction {
    object GetSaveListAction : SaveAction()
    data class AddSaveListAction(val name:String) : SaveAction()
    data class StorageAction(val storageParams: StorageParams) : SaveAction()
}