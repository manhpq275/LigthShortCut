package org.codevn.shortcut.data
import android.graphics.drawable.Drawable
import java.io.Serializable
import org.codevn.shortcut.R

enum class DataType(val value: Array<Any>) : Serializable {
    SILENT(arrayOf(
        R.drawable.ic_silent_active,
        R.drawable.ic_silent,
        R.drawable.ic_b_silent,
        R.drawable.bg_silent,
        "Silent Mode",
        "Switch between Silent and Ring for calls and alerts.")
    ),
    NOT_DISTURB(arrayOf(
        R.drawable.ic_not_disturb_active,
        R.drawable.ic_not_disturb,
        R.drawable.ic_b_not_sturb,
        R.drawable.bg_not_disturb,
        "Do Not Disturb",
        "Turn Focus on to silence notifications and filter out distractions.")
    ),
    CAMERA(arrayOf(
        R.drawable.ic_camera_active,
        R.drawable.ic_camera,
        R.drawable.ic_b_camera,
        R.drawable.bg_camera,
        "Camera",
        "Open the camera app to capture a moment.")
    ),
    FLASH(arrayOf(
        R.drawable.ic_flash_active,
        R.drawable.ic_flash,
        R.drawable.ic_b_flash,
        R.drawable.bg_flash,
        "Flash Light",
        "Turn on extra light when you need it.")
    ),
    MEMO(arrayOf(
        R.drawable.ic_memo_active,
        R.drawable.ic_memo,
        R.drawable.ic_b_memo,
        R.drawable.bg_memo,
        "Voice memo",
        "Record personal notes, musical ideas and more.")
    ),
    MAGNIFIER(arrayOf(
        R.drawable.ic_magnifier_active,
        R.drawable.ic_magnifier,
        R.drawable.ic_b_magnifier,
        R.drawable.bg_magnifier,
        "Magnifier",
        "Turn your phone into magnifying glass to zoom in on and detect object near you.")
    ),
    SHORTCUT(arrayOf(
        R.drawable.ic_shortcut_active,
        R.drawable.ic_shortcut,
        R.drawable.ic_b_shortcut,
        R.drawable.bg_short_cut,
        "Shortcut",
        "Open an app or run your favorite.")
    ),
    NO_ACTION(arrayOf(
        R.drawable.ic_no_action_active,
        R.drawable.ic_no_action,
        R.drawable.ic_b_no_action,
        R.drawable.bg_no_action,"",""));

    fun icon(): Int {
        return this.value[1] as Int
    }
    fun iconActive(): Int {
        return this.value[0] as Int
    }
    fun bubble(): Int {
        return this.value[2] as Int
    }
    fun getBackground(): Int {
        return this.value[3] as Int
    }

    fun title(): String {
        return this.value[4] as String
    }

    fun description(): String {
        return this.value[5] as String
    }

    fun getAdditionalOption(): Array<String> {
        return when (this.ordinal) {
            CAMERA.ordinal -> {
                arrayOf("Photos", "Camera")
            }
            SHORTCUT.ordinal -> {
                arrayOf("Choose a shortcut...")
            }
            else -> {
                arrayOf()
            }
        }
    }
}
data class ShortCutData(val position: Int, val icon: Int, val additionalOption: String): Serializable
data class AppListMain(val icon: Drawable, val appName: String, val packageName: String)