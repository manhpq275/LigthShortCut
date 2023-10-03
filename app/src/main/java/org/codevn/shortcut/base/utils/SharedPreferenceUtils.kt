package org.codevn.shortcut.base.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

abstract class SharedPreferenceUtils {
    protected lateinit var sharedPreference: SharedPreferences

    protected fun save(key: String, value: Any?) {
        val editor = sharedPreference.edit()
        if (value == null){
            editor.putString(key, null)
        } else {
            val valueStr = Gson().toJson(value)
            editor.putString(key, valueStr)
        }
        editor.apply()
    }

    protected fun<T> get(key: String): T? {
        val valueStr = sharedPreference.getString(key, null) ?: return null
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(valueStr, type)
    }

    protected fun<T> get(key: String, className: Class<T>): T? {
        val valueStr = sharedPreference.getString(key, null) ?: return null
        return Gson().fromJson(valueStr, className)
    }

    fun clearAll() {
        sharedPreference.edit().clear().apply()
    }
    fun clear() {
        sharedPreference.edit().clear().apply()
    }
}