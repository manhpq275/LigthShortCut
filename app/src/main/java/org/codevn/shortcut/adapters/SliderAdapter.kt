package org.codevn.shortcut.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.codevn.shortcut.R
import org.codevn.shortcut.base.BaseViewHolder
import org.codevn.shortcut.base.LoadingViewHolder
import org.codevn.shortcut.data.AdapterDataType
import org.codevn.shortcut.data.ShortCutData
import org.codevn.shortcut.databinding.ItemChooseBinding
import org.codevn.shortcut.databinding.ItemShortcutBinding


class SliderAdapter(
    val context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {
    private var data = ArrayList<ShortCutData>()
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var currentItem: Int = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            AdapterDataType.LOADING.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.item_loading_common,
                    parent,
                    false
                )
                LoadingViewHolder(view)
            }
            AdapterDataType.ITEM.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_shortcut, parent, false)
                ShortCutViewHolder(view)
            }
            AdapterDataType.CHOOSE.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_choose, parent, false)
                ChooseViewHolder(view)
            }
            else -> {
                val view = layoutInflater
                    .inflate(R.layout.item_loading_common, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reload(){
        notifyDataSetChanged()
    }
    fun setCurrentItem(index: Int) {
        val oldIndex = currentItem
        currentItem = index
        notifyItemChanged(oldIndex)
        notifyItemChanged(currentItem)
    }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is ShortCutViewHolder -> {
                holder.binding(position, data[position])
            }
            is ChooseViewHolder -> {
                holder.binding(position, data[position])
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return data[position].type.ordinal
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: List<ShortCutData>, isFirst: Boolean) {
        if (isFirst) {
            data.clear()
        }
        data.removeIf { it.type == AdapterDataType.LOADING }
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun addLoading() {
        val loading = ShortCutData(type = AdapterDataType.LOADING)
        data.add(loading)
        notifyItemInserted(data.indexOf(loading))
    }

    inner class ShortCutViewHolder(v: View) : BaseViewHolder(v, context) {
        private val binding: ItemShortcutBinding? = DataBindingUtil.bind(v)
        init {
            v.tag = binding
        }

        override fun binding(position: Int, data: ShortCutData) {
            binding?.let {
                it.item = data
            }
        }
    }
    inner class ChooseViewHolder(v: View) : BaseViewHolder(v, context) {
        private val binding: ItemChooseBinding? = DataBindingUtil.bind(v)
        init {
            v.tag = binding
        }

        override fun binding(position: Int, data: ShortCutData) {
            binding?.let {
                it.item = data
                if (currentItem == position) {
                    it.imgChoose.setImageResource(data.iconActive)
                } else {
                    it.imgChoose.setImageResource(data.icon)
                }
            }
        }
    }
}

