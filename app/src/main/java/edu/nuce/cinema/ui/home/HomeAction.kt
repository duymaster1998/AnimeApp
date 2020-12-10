package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviAction

sealed class HomeAction : MviAction {
    object GetTopCategoryAction : HomeAction()
    object GetAllSeriesAction : HomeAction()
    object GetTopAnimeAction : HomeAction()
    object GetNewAnimeAction : HomeAction()
}