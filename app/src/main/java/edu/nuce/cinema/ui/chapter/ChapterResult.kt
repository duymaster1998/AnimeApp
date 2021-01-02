package edu.nuce.cinema.ui.chapter

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.Series

sealed class ChapterResult : MviResult {

    sealed class GetChapterMangaResult : ChapterResult() {
        data class Success(val mangas: List<Manga>) : GetChapterMangaResult()
        data class Failure(val error: Throwable) : GetChapterMangaResult()
        object InFlight : GetChapterMangaResult()
    }
    sealed class UpdateMangaResult : ChapterResult() {
        data class Success(val manga: Manga) : UpdateMangaResult()
        data class Failure(val error: Throwable) : UpdateMangaResult()
        object InFlight : UpdateMangaResult()
    }
}