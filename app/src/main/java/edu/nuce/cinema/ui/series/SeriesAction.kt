package edu.nuce.cinema.ui.series

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Login

sealed class SeriesAction : MviAction{
    object GetSeriesAction : SeriesAction()
    data class GetSeriesByCategoryAction(val id:Int):SeriesAction()
    data class GetSeriesLikeAction(val input:String):SeriesAction()
    data class GetSeriesByCategoryLikeAction(val input: String,val id:Int):SeriesAction()
}