package org.codevn.shortcut.base

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import org.codevn.shortcut.R
import org.codevn.shortcut.data.DataType

open class BaseViewHolder(itemView: View, context: Context?) : RecyclerView.ViewHolder(itemView) {
    private var context: Context? = null

    init {
        this.context = context
    }

    open fun binding(position: Int, data: DataType) {
        val animation: Animation =
            AnimationUtils.loadAnimation(context, R.anim.item_animatior)
        animation.startOffset = 5 //Provide delay here
        itemView.startAnimation(animation)
    }
}