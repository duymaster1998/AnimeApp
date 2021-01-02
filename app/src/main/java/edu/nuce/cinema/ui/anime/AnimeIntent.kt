package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.data.models.request.HistoryParams
import edu.nuce.cinema.ui.episode.EpisodeIntent

sealed class AnimeIntent : MviIntent {
    data class UpdateAnimeIntent(val id: Int,val historyParams: HistoryParams): AnimeIntent()
    data class GetCommentIntent(val id:Int):AnimeIntent()
    data class GetSeriesIntent(val id:Int):AnimeIntent()
    data class GetArchiveIntent(val id:Int):AnimeIntent()
    data class CommentIntent(val commentParams: CommentParams,val id:Int):AnimeIntent()
    data class LikeIntent(val id:Int):AnimeIntent()
    object GetLikeMeIntent:AnimeIntent()
}
