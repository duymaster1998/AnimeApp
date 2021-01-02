package edu.nuce.cinema.ui.chapter

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime

sealed class ChapterIntent : MviIntent {
    data class GetChapterMangaIntent(val id: Int): ChapterIntent()
    data class UpdateMangaIntent(val id: Int): ChapterIntent()
}