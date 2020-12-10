package edu.nuce.cinema.ui.detail

import edu.nuce.base.mvi.MviAction

sealed class DetailAction:MviAction {
    data class GetCategoryBySeriesAction(val id:Int):DetailAction()
    data class GetRateSeriesAction(val id:Int):DetailAction()
    data class IsFollowAction(val id:Int):DetailAction()
    data class FollowAction(val id:Int):DetailAction()
}