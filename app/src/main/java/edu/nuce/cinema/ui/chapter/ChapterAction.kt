package edu.nuce.cinema.ui.chapter

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Anime

sealed class ChapterAction : MviAction {
    data class GetChapterMangaAction(val id: Int) : ChapterAction()
    data class UpdateMangaAction(val id: Int) : ChapterAction()
}