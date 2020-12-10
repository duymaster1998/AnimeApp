package edu.nuce.base.view.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

abstract class BaseViewModel<T : BaseState> : ViewModel() {

    private val disposables = CompositeDisposable()
    protected val initialState: T = this.initialState()

    abstract fun initialState(): T

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    protected fun Disposable.disposeOnDestroy(): Disposable {
        disposables.add(this)
        return this
    }

    protected fun <T : Any> getApiResponse(
        apiCall: Observable<T>,
        initialValue: T? = null
    ): Observable<ApiResponse<T>> {
        return apiCall
            .map { ApiResponse.success(data = it) }
            .startWithItem(ApiResponse.loading(data = initialValue))
            .onErrorReturn { ApiResponse.error(error = it, data = initialValue) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    inline fun <reified T> BehaviorSubject<T>.asObservable(): Observable<T> {
        return this.cast(T::class.java).observeOn(AndroidSchedulers.mainThread())
    }

    inline fun <reified T> PublishSubject<T>.asObservable(): Observable<T> {
        return this.cast(T::class.java).observeOn(AndroidSchedulers.mainThread())
    }

    inline fun <reified T> ReplaySubject<T>.asObservable(): Observable<T> {
        return this.cast(T::class.java).observeOn(AndroidSchedulers.mainThread())
    }

}