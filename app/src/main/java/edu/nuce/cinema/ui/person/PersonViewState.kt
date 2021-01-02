package edu.nuce.cinema.ui.person

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.base.mvi.MviViewState
import edu.nuce.cinema.data.models.Achievement
import edu.nuce.cinema.data.models.User

data class PersonViewState(
    val isLoading: Boolean,
    val error: Throwable?,
    val user: User?,
    val achievement: Achievement?
):MviViewState{
    companion object {
        fun initialState(): PersonViewState {
            return PersonViewState(
                isLoading = false,
                error = null,
                user = null,
                achievement = null
            )
        }
    }
}