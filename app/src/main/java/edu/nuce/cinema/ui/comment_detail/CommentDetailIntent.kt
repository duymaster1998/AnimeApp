package edu.nuce.cinema.ui.comment_detail

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.ui.anime.AnimeIntent
import edu.nuce.cinema.ui.episode.EpisodeIntent

sealed class CommentDetailIntent : MviIntent {
    data class GetCommentIntent(val id:Int):CommentDetailIntent()
    data class CommentIntent(val commentParams: CommentParams,val id:Int):CommentDetailIntent()
    data class LikeIntent(val id:Int): CommentDetailIntent()
    object GetLikeMeIntent: CommentDetailIntent()
}
