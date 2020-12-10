package edu.nuce.cinema.di

import edu.nuce.cinema.data.models.User
import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@LoggedUserScope
class UserDataRepository @Inject constructor(
    private val apiService: ApiService
) {

    private var _currentUser: User? = null
    val currentUser: User = _currentUser!!

    fun cacheUser(user: User) {
        _currentUser = user
    }

    fun getAuthUser(): Observable<User> {
        return apiService.getAuthUser()
            .toObservable()
    }

}