package edu.nuce.cinema.ui.anime

import edu.nuce.base.mvi.MviIntent

sealed class AnimeIntent : MviIntent {
    data class GetCategoryBySeriesIntent(val id:Int):AnimeIntent()
    data class GetRateSeriesIntent(val id:Int):AnimeIntent()
    data class GetSeriesIntent(val id: Int): AnimeIntent()
    data class GetSeriesAIntent(val id: Int): AnimeIntent()
}