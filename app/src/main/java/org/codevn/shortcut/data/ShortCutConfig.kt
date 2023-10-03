package org.codevn.shortcut.data

import android.content.Context
import android.hardware.SensorAdditionalInfo
import android.provider.ContactsContract.Data
import org.codevn.shortcut.base.utils.SharedPreferenceUtils

class ShortCutConfig(context: Context) : SharedPreferenceUtils() {
    companion object {
        private const val SHORT_CUT_CONFIG_PRIVATE_KEY = "ShortCutConfig"
        private const val SHORTCUT_SELECTED = "SHORTCUT_SELECTED"
    }
    init {
        sharedPreference =
            context.getSharedPreferences(SHORT_CUT_CONFIG_PRIVATE_KEY, Context.MODE_PRIVATE)
    }
    fun saveShortCut(position: Int, additionalInfo: String) {
        save(SHORTCUT_SELECTED, ShortCutData(position, 0, additionalInfo))
    }
    fun getShortCut(): ShortCutData? {
        return get(SHORTCUT_SELECTED, ShortCutData::class.java)
    }
}