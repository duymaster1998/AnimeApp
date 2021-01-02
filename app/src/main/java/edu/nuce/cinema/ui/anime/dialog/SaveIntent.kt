package edu.nuce.cinema.ui.anime.dialog

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.StorageParams

sealed class SaveIntent : MviIntent {
    object GetListSaveIntent: SaveIntent()
    data class AddListSaveIntent(val name:String): SaveIntent()
    data class StorageIntent(val storageParams: StorageParams): SaveIntent()
}