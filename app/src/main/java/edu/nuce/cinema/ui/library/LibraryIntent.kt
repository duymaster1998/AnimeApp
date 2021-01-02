package edu.nuce.cinema.ui.library

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime

sealed class LibraryIntent : MviIntent {
    object GetSubscribeSeriesIntent: LibraryIntent()
    object GetListSaveIntent: LibraryIntent()
    data class AddListSaveIntent(val name:String): LibraryIntent()
    data class RemoveArchiveIntent(val id:Int): LibraryIntent()
}