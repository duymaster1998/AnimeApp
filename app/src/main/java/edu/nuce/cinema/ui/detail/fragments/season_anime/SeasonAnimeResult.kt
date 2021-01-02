package edu.nuce.cinema.ui.detail.fragments.season_anime

import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.Season
import edu.nuce.cinema.ui.anime.AnimeResult


sealed class SeasonAnimeResult : MviResult{
    sealed class GetSeasonsResult: SeasonAnimeResult() {
        data class Success(val seasons: List<Season>) : GetSeasonsResult()
        data class Failure(val error: Throwable) : GetSeasonsResult()
        object InFlight : GetSeasonsResult()
    }
    sealed class GetTopCommentResult: SeasonAnimeResult() {
        data class Success(val comments: List<Comment>) : GetTopCommentResult()
        data class Failure(val error: Throwable) : GetTopCommentResult()
        object InFlight : GetTopCommentResult()
    }
    sealed class LikeMeResult: SeasonAnimeResult() {
        data class Success(val likes: List<Int>) : LikeMeResult()
        data class Failure(val error: Throwable) : LikeMeResult()
        object InFlight : LikeMeResult()
    }
}