package edu.nuce.cinema.ui.library_detail

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime

sealed class LibraryDetailIntent : MviIntent {
    object GetAnimeIntent: LibraryDetailIntent()
    object GetMangaIntent: LibraryDetailIntent()
    data class GetAnimeByArchiveIntent(val id:Int):LibraryDetailIntent()
    data class GetMangaByArchiveIntent(val id:Int):LibraryDetailIntent()
}