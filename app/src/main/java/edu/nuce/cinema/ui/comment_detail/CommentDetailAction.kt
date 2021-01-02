package edu.nuce.cinema.ui.comment_detail

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.ui.anime.AnimeAction

sealed class CommentDetailAction:MviAction {
    data class GetCommentAction(val id:Int):CommentDetailAction()
    data class CommentAction(val commentParams: CommentParams, val id:Int):CommentDetailAction()
    data class LikeAction(val id: Int): CommentDetailAction()
    object GetLikeAction: CommentDetailAction()
}