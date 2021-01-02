package edu.nuce.cinema.ui.person

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PersonActionProcessor @Inject constructor(
    private val _apiService: ApiService,
) {
    private val getUserProcessor =
        ObservableTransformer<PersonAction.GetUserAction, PersonResult.GetUserResult> { action ->
            action.flatMap { userAction ->
                _apiService.getAuthUser()
                    .toObservable()
                    .map { PersonResult.GetUserResult.Success(user = it) }
                    .cast(PersonResult.GetUserResult::class.java)
                    .onErrorReturn(PersonResult.GetUserResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(PersonResult.GetUserResult.InFlight)
            }
        }
    private val getAchievementActionProcessor =
        ObservableTransformer<PersonAction.GetAchievementAction,PersonResult.GetAchievementResult>{ action->
            action.flatMap {
                _apiService.getAchievements()
                    .toObservable()
                    .map { PersonResult.GetAchievementResult.Success(achievement = it) }
                    .cast(PersonResult.GetAchievementResult::class.java)
                    .onErrorReturn(PersonResult.GetAchievementResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(PersonResult.GetAchievementResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<PersonAction, PersonResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(PersonAction.GetAchievementAction::class.java).compose(getAchievementActionProcessor),
                shared.ofType(PersonAction.GetUserAction::class.java).compose(getUserProcessor)
            )
        }
    }
}