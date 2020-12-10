package edu.nuce.base.delegates

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is Boolean -> putBoolean(key, value as Boolean)
        is String -> putString(key, value as String)
        is Int -> putInt(key, value as Int)
        is Short -> putShort(key, value as Short)
        is Long -> putLong(key, value as Long)
        is Byte -> putByte(key, value as Byte)
        is ByteArray -> putByteArray(key, value as ByteArray)
        is Char -> putChar(key, value as Char)
        is CharArray -> putCharArray(key, value as CharArray)
        is CharSequence -> putCharSequence(key, value as CharSequence)
        is Float -> putFloat(key, value as Float)
        is Bundle -> putBundle(key, value as Bundle)
        is Parcelable -> putParcelable(key, value as Parcelable)
        is Serializable -> putSerializable(key, value as Serializable)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

class FragmentArgumentProperty<T : Any>(private val key: String) : ReadWriteProperty<Fragment, T> {

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        args.put(key, value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.arguments?.get(key) as? T
            ?: throw IllegalStateException("Property $key could not be read")
    }

}

class FragmentNullableArgumentProperty<T : Any?>(private val key: String) :
    ReadWriteProperty<Fragment, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): T? {
        return thisRef.arguments?.get(key) as? T
    }

    override fun setValue(
        thisRef: Fragment,
        property: KProperty<*>, value: T?
    ) {
        val args = thisRef.arguments
            ?: Bundle().also(thisRef::setArguments)
        value?.let { args.put(key, it) } ?: args.remove(key)
    }

}

fun <T : Any> argNotNull(key: String): ReadWriteProperty<Fragment, T> =
    FragmentArgumentProperty(key)

fun <T : Any?> arg(key: String): ReadWriteProperty<Fragment, T?> =
    FragmentNullableArgumentProperty(key)