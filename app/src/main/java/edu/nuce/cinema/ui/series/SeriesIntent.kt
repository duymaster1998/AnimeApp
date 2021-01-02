package edu.nuce.cinema.ui.series

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Login

sealed class SeriesIntent : MviIntent {
    object GetSeriesIntent : SeriesIntent()
    data class GetSeriesByCategoryIntent(val id:Int): SeriesIntent()
    data class GetSeriesLikeIntent(val input:String): SeriesIntent()
    data class GetSeriesByCategoryLikeIntent(val input: String,val id:Int): SeriesIntent()
}