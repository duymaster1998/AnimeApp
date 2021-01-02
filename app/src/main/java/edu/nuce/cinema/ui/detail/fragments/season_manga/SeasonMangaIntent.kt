package edu.nuce.cinema.ui.detail.fragments.season_manga

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeIntent

sealed class SeasonMangaIntent : MviIntent {
    data class GetSeasonsIntent(val id:Int):SeasonMangaIntent()
    data class GetTopCommentIntent(val id:Int):SeasonMangaIntent()
    data class LikeIntent(val id:Int): SeasonMangaIntent()
    object GetLikeMeIntent: SeasonMangaIntent()
}
