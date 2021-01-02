package edu.nuce.cinema.ui.person

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Login

sealed class PersonIntent : MviIntent {
    object GetUserIntent : PersonIntent()
    object GetAchievementIntent: PersonIntent()
}