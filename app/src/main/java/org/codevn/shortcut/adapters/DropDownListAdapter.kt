package org.codevn.shortcut.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jaredrummler.materialspinner.MaterialSpinnerBaseAdapter
import org.codevn.shortcut.R


class DropDownListAdapter(
    private val context: Context,
    private val icons: Array<Int>,
    var names: Array<String>,
    private val iconsApp: Array<Drawable> = arrayOf()
): MaterialSpinnerBaseAdapter(context), Parcelable {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    constructor(parcel: Parcel) : this(
        TODO("context"),
        TODO("icons"),
        parcel.createStringArray(),
        TODO("iconsApp")
    ) {

    }

    override fun getCount(): Int {
        if (iconsApp.isNotEmpty()) {
            return iconsApp.size
        }
        return icons.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = inflater.inflate(R.layout.custom_spinner_items, null)
        val icon: ImageView? = view?.findViewById(R.id.imageView)
        if (iconsApp.isNotEmpty()) {
            icon?.setImageDrawable(iconsApp[p0])
        } else {
            icon?.setImageResource(icons[p0])
        }
        view?.findViewById<TextView>(R.id.textView)?.text = names[p0]
        return view!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(names)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DropDownListAdapter> {
        override fun createFromParcel(parcel: Parcel): DropDownListAdapter {
            return DropDownListAdapter(parcel)
        }

        override fun newArray(size: Int): Array<DropDownListAdapter?> {
            return arrayOfNulls(size)
        }
    }
}