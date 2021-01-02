package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Season
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.ui.episode.EpisodeResult


sealed class MangaResult : MviResult{
    sealed class UpdateMangaResult : MangaResult() {
        data class Success(val Manga: Manga) : UpdateMangaResult()
        data class Failure(val error: Throwable) : UpdateMangaResult()
        object InFlight : UpdateMangaResult()
    }
    sealed class GetSeriesResult: MangaResult() {
        data class Success(val series: Series) : GetSeriesResult()
        data class Failure(val error: Throwable) : GetSeriesResult()
        object InFlight : GetSeriesResult()
    }
    sealed class GetArchiveResult: MangaResult() {
        data class Success(val archives: List<Int>) : GetArchiveResult()
        data class Failure(val error: Throwable) : GetArchiveResult()
        object InFlight : GetArchiveResult()
    }
}