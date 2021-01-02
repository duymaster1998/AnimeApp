package edu.nuce.cinema.ui.person

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Login

sealed class PersonAction : MviAction{
    object GetUserAction : PersonAction()
    object GetAchievementAction:PersonAction()
}