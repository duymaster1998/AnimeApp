package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Season
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.ui.episode.EpisodeResult


sealed class AnimeResult : MviResult{
    sealed class UpdateAnimeResult : AnimeResult() {
        data class Success(val anime: Anime) : UpdateAnimeResult()
        data class Failure(val error: Throwable) : UpdateAnimeResult()
        object InFlight : UpdateAnimeResult()
    }
    sealed class GetCommentResult: AnimeResult() {
        data class Success(val comments: List<Comment>) : GetCommentResult()
        data class Failure(val error: Throwable) : GetCommentResult()
        object InFlight : GetCommentResult()
    }
    sealed class GetSeriesResult: AnimeResult() {
        data class Success(val series: Series) : GetSeriesResult()
        data class Failure(val error: Throwable) : GetSeriesResult()
        object InFlight : GetSeriesResult()
    }
    sealed class GetArchiveResult: AnimeResult() {
        data class Success(val archives: List<Int>) : GetArchiveResult()
        data class Failure(val error: Throwable) : GetArchiveResult()
        object InFlight : GetArchiveResult()
    }
    sealed class CommentResult: AnimeResult() {
        data class Success(val comments: List<Comment>) : CommentResult()
        data class Failure(val error: Throwable) : CommentResult()
        object InFlight : CommentResult()
    }
    sealed class LikeMeResult: AnimeResult() {
        data class Success(val likes: List<Int>) : LikeMeResult()
        data class Failure(val error: Throwable) : LikeMeResult()
        object InFlight : LikeMeResult()
    }
}