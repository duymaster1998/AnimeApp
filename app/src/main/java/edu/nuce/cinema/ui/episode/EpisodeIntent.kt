package edu.nuce.cinema.ui.episode

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime

sealed class EpisodeIntent : MviIntent {
    data class GetEpisodeAnimeIntent(val id: Int): EpisodeIntent()
    data class UpdateAnimeIntent(val id: Int): EpisodeIntent()
}