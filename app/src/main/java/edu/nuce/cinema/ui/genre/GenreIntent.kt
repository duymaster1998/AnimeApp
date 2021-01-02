package edu.nuce.cinema.ui.genre

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Login

sealed class GenreIntent : MviIntent {
    object GetGenreIntent : GenreIntent()
    data class GetGenreByLikeIntent(val input:String): GenreIntent()
}