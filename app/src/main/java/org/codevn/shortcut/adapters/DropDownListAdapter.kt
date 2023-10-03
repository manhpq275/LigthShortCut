package org.codevn.shortcut.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import org.codevn.shortcut.R


class DropDownListAdapter(private val context: Context, private val icons: Array<Int>, var names: Array<String>):
    BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
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
        icon?.setImageResource(icons[p0])
        view?.findViewById<TextView>(R.id.textView)?.text = names[p0]
        return view!!
    }
}