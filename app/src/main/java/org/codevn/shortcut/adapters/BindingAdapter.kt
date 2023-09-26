package org.codevn.shortcut.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("image")
fun ImageView.setImageFromResource(id: Int) {
    this.setImageResource(id)
}

