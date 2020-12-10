package edu.nuce.base.utils

import android.content.Context
import java.math.BigDecimal

object SharedPreferencesUtils {

    private const val name = "edu_nuce"

    fun clear(context: Context, key: String?) {
        val preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        preferences.edit().remove(key).apply()
    }

    fun clearAll(context: Context) {
        val preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        preferences.edit().clear().apply()
    }

    fun putString(context: Context, key: String?, value: String?) {
        val settings = context.getSharedPreferences(name, 0)
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String?): String? {
        var result = key
        val settings = context.getSharedPreferences(name, 0)
        result = settings.getString(key, "")
        return result
    }

    fun getString(context: Context, key: String?, defaultVaule: String?): String? {
        var result = defaultVaule
        val settings = context.getSharedPreferences(name, 0)
        result = settings.getString(key, defaultVaule)
        return result
    }

    fun putBigDecimal(context: Context, key: String?, value: BigDecimal) {
        val settings = context.getSharedPreferences(name, 0)
        val editor = settings.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }

    fun getBigDecimal(context: Context, key: String?): BigDecimal {
        var result = key
        val settings = context.getSharedPreferences(name, 0)
        result = settings.getString(key, "0")
        return BigDecimal(result)
    }

    fun getBigDecimal(context: Context, key: String?, defaultVaule: BigDecimal): BigDecimal {
        var result: BigDecimal? = defaultVaule
        val settings = context.getSharedPreferences(name, 0)
        result = BigDecimal(settings.getString(key, defaultVaule.toString()))
        return result
    }

    fun putLong(context: Context, key: String?, value: Long?) {
        val settings = context.getSharedPreferences(name, 0)
        val editor = settings.edit()
        editor.putLong(key, value!!)
        editor.apply()
    }

    fun getLong(context: Context, key: String?): Long {
        val result: Long
        val settings = context.getSharedPreferences(name, 0)
        result = settings.getLong(key, 0)
        return result
    }

    fun getLong(context: Context, key: String?, value: Long): Long {
        var result = value
        val settings = context.getSharedPreferences(name, 0)
        result = settings.getLong(key, 0)
        return result
    }

    fun putInt(context: Context, key: String?, value: Int) {
        val settings = context.getSharedPreferences(name, 0)
        val editor = settings.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(context: Context, key: String?): Int {
        val result: Int
        result = try {
            val settings = context.getSharedPreferences(name, 0)
            settings.getInt(key, 0)
        } catch (e: Exception) {
            0
        }
        return result
    }

    fun putFloat(context: Context, key: String?, value: Float) {
        val settings = context.getSharedPreferences(name, 0)
        val editor = settings.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }

    fun getFloat(context: Context, key: String?, value: Float): Float {
        var result = value
        result = try {
            val settings = context.getSharedPreferences(name, 0)
            java.lang.Float.valueOf(settings.getString(key, result.toString())!!)
        } catch (e: Exception) {
            0f
        }
        return result
    }

    fun putBoolean(context: Context, key: String?, value: Boolean?) {
        val settings = context.getSharedPreferences(name, 0)
        val editor = settings.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String?): Boolean {
        val result: Boolean
        val settings = context.getSharedPreferences(name, 0)
        result = settings.getBoolean(key, false)
        return result
    }

    fun getBoolean(context: Context, key: String?, value: Boolean): Boolean {
        val result: Boolean
        val settings = context.getSharedPreferences(name, 0)
        result = settings.getBoolean(key, value)
        return result
    }

    fun getInt(context: Context, key: String?, defaultValue: Int): Int {
        val result: Int
        result = try {
            val settings = context.getSharedPreferences(name, 0)
            settings.getInt(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
        return result
    }
}