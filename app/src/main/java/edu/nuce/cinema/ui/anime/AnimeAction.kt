package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.ui.home.HomeAction

sealed class AnimeAction:MviAction {
    data class GetCategoryBySeriesAction(val id:Int):AnimeAction()
    data class GetRateSeriesAction(val id:Int):AnimeAction()
    data class GetSeriesAction(val id: Int) : AnimeAction()
    data class GetSeriesAAction(val id: Int) : AnimeAction()

}