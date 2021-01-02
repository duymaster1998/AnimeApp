package edu.nuce.cinema.ui.comment_manga

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.ui.anime.AnimeAction

sealed class CommentMangaAction:MviAction {
    data class GetCommentAction(val id:Int):CommentMangaAction()
    data class CommentAction(val commentParams: CommentParams, val id:Int):CommentMangaAction()
    data class GetSeriesAction(val id: Int): CommentMangaAction()
    data class LikeAction(val id: Int):CommentMangaAction()
    object GetLikeAction:CommentMangaAction()
}