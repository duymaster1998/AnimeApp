package edu.nuce.cinema.ui.report

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Login
import edu.nuce.cinema.data.models.request.ReportParams

sealed class ReportCommentIntent : MviIntent {
    data class CheckReportIntent(val id:Int) : ReportCommentIntent()
    data class ReportIntent(val reportParams: ReportParams,val id: Int): ReportCommentIntent()
}