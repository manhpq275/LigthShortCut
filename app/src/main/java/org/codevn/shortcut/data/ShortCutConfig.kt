package org.codevn.shortcut.data

import android.content.Context
import android.hardware.SensorAdditionalInfo
import android.provider.ContactsContract.Data
import org.codevn.shortcut.base.utils.SharedPreferenceUtils

class ShortCutConfig(context: Context) : SharedPreferenceUtils() {
    companion object {
        private const val SHORT_CUT_CONFIG_PRIVATE_KEY = "ShortCutConfig"
        private const val SHORTCUT_SELECTED = "SHORTCUT_SELECTED"
        private const val SHORTCUT_CAMERA_NEAR = "SHORTCUT_CAMERA"
        private const val SHORTCUT_APP_NEAR = "SHORTCUT_APP_NEAR"
    }
    init {
        sharedPreference =
            context.getSharedPreferences(SHORT_CUT_CONFIG_PRIVATE_KEY, Context.MODE_PRIVATE)
    }
    fun saveShortCut(data: ShortCutData) {
        save(SHORTCUT_SELECTED, data)
    }

    fun saveCameraNear(data: ShortCutData) {
        save(SHORTCUT_CAMERA_NEAR, data)
    }
    fun getCameraNear():ShortCutData {
        return get(SHORTCUT_CAMERA_NEAR, ShortCutData::class.java) ?: ShortCutData(0, 0, "")
    }

    fun saveAppNear(data: ShortCutData) {
        save(SHORTCUT_APP_NEAR, data)
    }
    fun getAppNear():ShortCutData {
        return get(SHORTCUT_APP_NEAR, ShortCutData::class.java) ?: ShortCutData(0, 0, "")
    }

    fun getShortCut(): ShortCutData {
        return get(SHORTCUT_SELECTED, ShortCutData::class.java) ?: ShortCutData(0, 0, "")
    }
}