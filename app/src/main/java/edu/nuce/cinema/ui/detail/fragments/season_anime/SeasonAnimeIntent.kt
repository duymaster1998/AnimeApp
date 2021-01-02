package edu.nuce.cinema.ui.detail.fragments.season_anime

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.ui.anime.AnimeIntent

sealed class SeasonAnimeIntent : MviIntent {
    data class GetSeasonsIntent(val id:Int):SeasonAnimeIntent()
    data class GetTopCommentIntent(val id:Int):SeasonAnimeIntent()
    data class LikeIntent(val id:Int): SeasonAnimeIntent()
    object GetLikeMeIntent:SeasonAnimeIntent()
}
