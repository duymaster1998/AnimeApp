package edu.nuce.cinema.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import edu.nuce.base.extensions.hideKeyboard
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragment(@LayoutRes resId: Int) : Fragment(resId) {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    protected fun Disposable.disposeOnDestroy(): Disposable {
        disposables.add(this)
        return this
    }
}