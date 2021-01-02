package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.data.models.request.HistoryParams

sealed class AnimeAction:MviAction {
    data class UpdateAnimeAction(val id: Int,val historyParams: HistoryParams) : AnimeAction()
    data class GetCommentAction(val id:Int):AnimeAction()
    data class CommentAction(val commentParams: CommentParams,val id:Int):AnimeAction()
    data class GetSeriesAction(val id: Int):AnimeAction()
    data class GetArchiveAction(val id: Int):AnimeAction()
    data class LikeAction(val id: Int):AnimeAction()
    object GetLikeAction:AnimeAction()
}