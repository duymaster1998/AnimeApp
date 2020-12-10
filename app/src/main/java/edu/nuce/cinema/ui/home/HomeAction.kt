package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviAction

sealed class HomeAction : MviAction {
//    data class GetSeriesAction(val id: Int) : HomeAction()
    object GetTopCategoryAction : HomeAction()
    object GetNewSeriesAction : HomeAction()
    object GetTopAnimeAction : HomeAction()
    object GetTopMangaAction : HomeAction()
    object GetNewAnimeAction : HomeAction()
    object GetNewMangaAction : HomeAction()
}