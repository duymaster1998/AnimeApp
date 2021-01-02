package edu.nuce.cinema.ui.library_detail

import edu.nuce.base.mvi.MviAction

sealed class LibraryDetailAction : MviAction {
    object GetAnimeAction: LibraryDetailAction()
    object GeMangaAction : LibraryDetailAction()
    data class GetAnimeByArchiveAction(val id:Int):LibraryDetailAction()
    data class GetMangaByArchiveAction(val id:Int):LibraryDetailAction()
}