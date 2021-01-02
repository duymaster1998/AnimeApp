package edu.nuce.cinema.ui.detail.fragments.description

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Rating

sealed class DescriptionIntent : MviIntent {
    data class GetRecommendIntent(val id:Int):DescriptionIntent()
    data class GetRateSeriesIntent(val id:Int):DescriptionIntent()
    data class GetAuthorIntent(val id:Int):DescriptionIntent()
    data class GetRateMeIntent(val id:Int):DescriptionIntent()
    data class RatingIntent(val rating: Rating,val id:Int):DescriptionIntent()
}