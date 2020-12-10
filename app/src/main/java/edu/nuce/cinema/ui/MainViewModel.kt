//package edu.nuce.cinema.ui

import edu.nuce.base.view.viewmodels.ApiResponse
import edu.nuce.base.view.viewmodels.BaseState

//data class MainState(
//    val users: ApiResponse<List<User>> = ApiResponse.uninitialized()
//) : BaseState()

//class MainViewModel @ViewModelInject constructor(
//    private val apiService: ApiService
//) : BaseViewModel<MainState>() {
//
//    private val _state = BehaviorSubject.createDefault(initialState)
//    val state: Observable<MainState> get() = _state.asObservable()
//
//    override fun initialState(): MainState = MainState()
//
//    init {
//        getUsers()
//    }
//
//    fun getUsers() {
//        getApiResponse(apiService.getUsers())
//            .subscribe {
//                next(initialState.copy(users = it))
//            }.disposeOnDestroy()
//    }
//
//    private fun next(state: MainState) {
//        _state.onNext(state)
//    }
//
//}