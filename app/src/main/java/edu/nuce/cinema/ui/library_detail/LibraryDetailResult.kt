package edu.nuce.cinema.ui.library_detail

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.*

sealed class LibraryDetailResult : MviResult {

    sealed class GetAnimeResult : LibraryDetailResult() {
        data class Success(val animes: List<Anime>) : GetAnimeResult()
        data class Failure(val error: Throwable) : GetAnimeResult()
        object InFlight : GetAnimeResult()
    }
    sealed class GetMangaResult : LibraryDetailResult() {
        data class Success(val mangas: List<Manga>) : GetMangaResult()
        data class Failure(val error: Throwable) : GetMangaResult()
        object InFlight : GetMangaResult()
    }
}