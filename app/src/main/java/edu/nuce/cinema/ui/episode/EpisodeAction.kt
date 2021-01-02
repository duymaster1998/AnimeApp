package edu.nuce.cinema.ui.episode

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Anime

sealed class EpisodeAction : MviAction {
    data class GetEpisodeAnimeAction(val id: Int) : EpisodeAction()
    data class UpdateAnimeAction(val id: Int) : EpisodeAction()
}