package org.codevn.shortcut.data
import java.io.Serializable
import org.codevn.shortcut.R

enum class AdapterDataType : Serializable {
    LOADING,
    ITEM,
    CHOOSE
}
enum class DataType : Serializable {
    SILENT,
    NOT_DISTURB,
    CAMERA,
    FLASH,
    MEMO,
    MAGNIFIER,
    SHORTCUT,
    NO_ACTION
}
class ShortCutData(
    val iconActive: Int = R.drawable.ic_silent_active,
    val icon: Int = R.drawable.ic_silent,
    val bubble: Int = R.drawable.ic_b_silent,
    val background: Int = R.drawable.bg_silent,
    val type: AdapterDataType = AdapterDataType.ITEM) : Serializable
