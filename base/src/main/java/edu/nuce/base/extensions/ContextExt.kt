package edu.nuce.base.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

inline fun <reified T : Any> simpleName(): String {
    return T::class.java.simpleName
}

inline fun <reified T : Activity> Context.intentFor(body: Intent.() -> Unit = {}): Intent =
    Intent(this, T::class.java).apply(body)

inline fun <reified T : Activity> Context.startActivity(body: Intent.() -> Unit = {}) {
    startActivity(intentFor<T>(body))
}

inline fun <reified T : Fragment> instance(body: Bundle.() -> Unit = {}): T {
    return T::class.java.newInstance().apply {
        arguments = Bundle().apply(body)
    }
}

fun Activity.toggleKeyboardVisibility() {
    toggleKeyboardVisibility(currentFocus ?: View(this))
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Fragment.toast(message: String, time: Int = Toast.LENGTH_SHORT) {
    activity?.toast(message, time)
}

fun Fragment.onBackPressed() {
    activity?.onBackPressed()
}

fun Fragment.toggleKeyboardVisibility() {
    view?.let { activity?.toggleKeyboardVisibility(it) }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.toggleKeyboardVisibility(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val connection = manager.activeNetworkInfo
    return connection != null && connection.isConnectedOrConnecting
}

fun Context.toast(message: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, time).show()
}

fun Context.formatMessage(format: Int, message: Int): String {
    return String.format(
        this.resources.getString(format),
        this.resources.getString(message)
    )
}