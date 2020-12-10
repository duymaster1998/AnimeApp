package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviIntent

sealed class HomeIntent : MviIntent {
    object GetAllSeriesIntent : HomeIntent()
    object GetTopAnimeIntent : HomeIntent()
    object GetTopCategoryIntent : HomeIntent()
    object GetNewAnimeIntent : HomeIntent()
}