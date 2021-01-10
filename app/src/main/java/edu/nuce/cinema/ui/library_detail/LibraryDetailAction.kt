package edu.nuce.cinema.ui.library_detail

import edu.nuce.base.mvi.MviAction

sealed class LibraryDetailAction : MviAction {
    object GetAnimeAction: LibraryDetailAction()
    data class RemoveAnimeAction(val id: Int): LibraryDetailAction()
    data class RemoveMangaAction(val id: Int): LibraryDetailAction()
    data class RemoveAnimeStorageAction(val animeId: Int,val id: Int,): LibraryDetailAction()
    data class RemoveMangaStorageAction(val mangaId: Int,val id: Int): LibraryDetailAction()
    object GeMangaAction : LibraryDetailAction()
    data class GetAnimeByArchiveAction(val id:Int):LibraryDetailAction()
    data class GetMangaByArchiveAction(val id:Int):LibraryDetailAction()
}