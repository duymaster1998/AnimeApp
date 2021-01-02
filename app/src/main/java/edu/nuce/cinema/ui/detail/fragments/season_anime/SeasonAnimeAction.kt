package edu.nuce.cinema.ui.detail.fragments.season_anime

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.ui.anime.AnimeAction

sealed class SeasonAnimeAction:MviAction {
    data class GetSeasonsAction(val id:Int):SeasonAnimeAction()
    data class GetTopCommentAction(val id:Int):SeasonAnimeAction()
    data class LikeAction(val id: Int): SeasonAnimeAction()
    object GetLikeAction:SeasonAnimeAction()
}