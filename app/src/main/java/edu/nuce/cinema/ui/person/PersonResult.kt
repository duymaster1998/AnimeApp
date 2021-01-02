package edu.nuce.cinema.ui.person

import edu.nuce.base.auth.DefaultOAuthCredentials
import edu.nuce.base.auth.OAuthCredentials
import edu.nuce.base.mvi.MviResult
import edu.nuce.cinema.data.models.Achievement
import edu.nuce.cinema.data.models.User

sealed class PersonResult : MviResult{
    sealed class GetAchievementResult:PersonResult(){
        data class Success(val achievement: Achievement) : GetAchievementResult()
        data class Failure(val error: Throwable) : GetAchievementResult()
        object InFlight : GetAchievementResult()
    }
    sealed class GetUserResult: PersonResult() {
        data class Success(val user: User) : GetUserResult()
        data class Failure(val error: Throwable) : GetUserResult()
        object InFlight : GetUserResult()
    }
}