package edu.nuce.cinema.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import edu.nuce.base.utils.NetworkEvent
import edu.nuce.base.utils.NetworkState
import edu.nuce.cinema.ui.dialog.SocketTimeoutDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

abstract class BaseActivity(@LayoutRes resId: Int) : AppCompatActivity(resId) {

    private val disposables = CompositeDisposable()

    private fun setWindowFlag(on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags =
                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }

    override fun onResume() {
        super.onResume()
        NetworkEvent.register(this) {
            when (it!!) {
                is NetworkState.BadRequest -> {
//                    Timber.e((it as NetworkState.BadRequest).exception.message)
                }
                is NetworkState.RequestTimeout -> {
//                    SocketTimeoutDialog.from((it as NetworkState.RequestTimeout).exception.message!!)
                }
                is NetworkState.NoResponse -> {

                }
                is NetworkState.NoInternet -> {
                    SocketTimeoutDialog.from("No Internet")
                }
                is NetworkState.Unauthorized -> {

                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        NetworkEvent.unregister(this)
    }

    override fun onDestroy() {
        disposables.clear()
        System.gc()
        Runtime.getRuntime().gc()
        super.onDestroy()
    }

    override fun onLowMemory() {
        System.gc()
        Runtime.getRuntime().gc()
        super.onLowMemory()
    }

    protected fun Disposable.disposeOnDestroy(): Disposable {
        disposables.add(this)
        return this
    }
}