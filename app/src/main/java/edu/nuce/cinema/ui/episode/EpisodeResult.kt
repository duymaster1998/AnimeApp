package edu.nuce.cinema.ui.episode

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series

sealed class EpisodeResult : MviResult {

    sealed class GetEpisodeAnimeResult : EpisodeResult() {
        data class Success(val animes: List<Anime>) : GetEpisodeAnimeResult()
        data class Failure(val error: Throwable) : GetEpisodeAnimeResult()
        object InFlight : GetEpisodeAnimeResult()
    }
    sealed class UpdateAnimeResult : EpisodeResult() {
        data class Success(val anime: Anime) : UpdateAnimeResult()
        data class Failure(val error: Throwable) : UpdateAnimeResult()
        object InFlight : UpdateAnimeResult()
    }
}