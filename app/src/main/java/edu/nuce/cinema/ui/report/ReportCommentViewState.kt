package edu.nuce.cinema.ui.report

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Achievement
import edu.nuce.cinema.data.models.User

data class ReportCommentViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val checkReport: Boolean,
):MviViewState{
    companion object {
        fun initialState(): ReportCommentViewState {
            return ReportCommentViewState(
                isLoading = false,
                error = null,
                checkReport = false,
            )
        }
    }
}