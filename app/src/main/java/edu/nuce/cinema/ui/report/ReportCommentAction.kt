package edu.nuce.cinema.ui.report

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Login
import edu.nuce.cinema.data.models.request.ReportParams

sealed class ReportCommentAction : MviAction{
    data class CheckReportAction(val id:Int) : ReportCommentAction()
    data class ReportAction(val reportParams: ReportParams,val id: Int):ReportCommentAction()
}