package edu.nuce.cinema.ui.comment_manga

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Season
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.ui.anime.AnimeResult
import edu.nuce.cinema.ui.episode.EpisodeResult


sealed class CommentMangaResult : MviResult{

    sealed class GetCommentResult: CommentMangaResult() {
        data class Success(val comments: List<Comment>) : GetCommentResult()
        data class Failure(val error: Throwable) : GetCommentResult()
        object InFlight : GetCommentResult()
    }
    sealed class GetSeriesResult: CommentMangaResult() {
        data class Success(val series: Series) : GetSeriesResult()
        data class Failure(val error: Throwable) : GetSeriesResult()
        object InFlight : GetSeriesResult()
    }
    sealed class LikeMeResult: CommentMangaResult() {
        data class Success(val likes: List<Int>) : LikeMeResult()
        data class Failure(val error: Throwable) : LikeMeResult()
        object InFlight : LikeMeResult()
    }
}