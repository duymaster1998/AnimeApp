package edu.nuce.cinema.ui.detail.fragments.description

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Rating

sealed class DescriptionAction:MviAction {
    data class GetRecommendAction(val id:Int):DescriptionAction()
    data class GetRateSeriesAction(val id:Int):DescriptionAction()
    data class GetAuthorAction(val id:Int):DescriptionAction()
    data class GetRateMeAction(val id:Int):DescriptionAction()
    data class RatingAction(val rating: Rating,val id:Int):DescriptionAction()
}