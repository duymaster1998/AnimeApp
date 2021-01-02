package edu.nuce.cinema.ui.comment_manga

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.ui.anime.AnimeIntent
import edu.nuce.cinema.ui.episode.EpisodeIntent

sealed class CommentMangaIntent : MviIntent {
    data class GetCommentIntent(val id:Int):CommentMangaIntent()
    data class CommentIntent(val commentParams: CommentParams,val id:Int):CommentMangaIntent()
    data class GetSeriesIntent(val id:Int): CommentMangaIntent()
    data class LikeIntent(val id:Int):CommentMangaIntent()
    object GetLikeMeIntent:CommentMangaIntent()
}
