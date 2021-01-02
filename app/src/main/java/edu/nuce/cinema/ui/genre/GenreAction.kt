package edu.nuce.cinema.ui.genre

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Login

sealed class GenreAction : MviAction{
    object GetGenreAction : GenreAction()
    data class GetGenreByLikeAction(val input:String):GenreAction()
}