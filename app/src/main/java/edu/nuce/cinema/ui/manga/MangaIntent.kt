package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviIntent

sealed class MangaIntent : MviIntent {
    data class GetCategoryBySeriesIntent(val id:Int):MangaIntent()
    data class GetRateSeriesIntent(val id:Int):MangaIntent()
}