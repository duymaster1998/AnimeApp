package edu.nuce.cinema.ui.detail

import edu.nuce.base.mvi.MviIntent

sealed class DetailIntent : MviIntent {
    data class GetCategoryBySeriesIntent(val id:Int):DetailIntent()
    data class GetRateSeriesIntent(val id:Int):DetailIntent()
    data class IsFollowIntent(val id:Int):DetailIntent()
    data class FollowIntent(val id:Int):DetailIntent()
}
