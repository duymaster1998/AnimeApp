package edu.nuce.cinema.ui.home

import edu.nuce.base.mvi.MviIntent

sealed class HomeIntent : MviIntent {
    object GetNewSeriesIntent : HomeIntent()
    object GetTopAnimeIntent : HomeIntent()
    object GetTopMangaIntent : HomeIntent()
    object GetTopCategoryIntent : HomeIntent()
//    data class GetSeriesIntent(val id: Int): HomeIntent()
    object GetNewAnimeIntent : HomeIntent()
    object GetNewMangaIntent : HomeIntent()
}