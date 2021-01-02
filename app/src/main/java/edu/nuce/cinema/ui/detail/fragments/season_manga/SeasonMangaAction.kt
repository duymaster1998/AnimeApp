package edu.nuce.cinema.ui.detail.fragments.season_manga

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeAction

sealed class SeasonMangaAction:MviAction {
    data class GetSeasonsAction(val id:Int):SeasonMangaAction()
    data class GetTopCommentAction(val id:Int):SeasonMangaAction()
    data class LikeAction(val id: Int): SeasonMangaAction()
    object GetLikeAction: SeasonMangaAction()
}