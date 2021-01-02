package edu.nuce.cinema.ui.report

import edu.nuce.base.mvi.MviResult

sealed class ReportCommentResult : MviResult {

    sealed class CheckReportResult : ReportCommentResult() {
        data class Success(val checkReport: Boolean) : CheckReportResult()
        data class Failure(val error: Throwable) : CheckReportResult()
        object InFlight : CheckReportResult()
    }
}