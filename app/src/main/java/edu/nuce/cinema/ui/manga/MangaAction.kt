package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviAction

sealed class MangaAction:MviAction {
    data class GetCategoryBySeriesAction(val id:Int):MangaAction()
    data class GetRateSeriesAction(val id:Int):MangaAction()
}