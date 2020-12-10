package edu.nuce.base.delegates

import android.app.Activity

inline fun <reified T: Any> Activity.arg(key: String, default: T? = null) = lazy(LazyThreadSafetyMode.NONE) {
    val value = intent?.extras?.get(key)
    if (value is T) value else default
}

inline fun <reified T: Any> Activity.argNotNull(key: String, default: T? = null) = lazy(LazyThreadSafetyMode.NONE) {
    val value = intent?.extras?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}