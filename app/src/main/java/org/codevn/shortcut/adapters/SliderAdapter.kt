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
import org.codevn.shortcut.data.DataType
import org.codevn.shortcut.databinding.ItemChooseBinding


class SliderAdapter(
    val context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {
    private var data = ArrayList<DataType>()
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var currentItem: Int = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = layoutInflater.inflate(R.layout.item_choose, parent, false)
        return ChooseViewHolder(view)

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
            is ChooseViewHolder -> {
                holder.binding(position, data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: List<DataType>, isFirst: Boolean) {
        if (isFirst) {
            data.clear()
        }
        data.addAll(items)
        notifyDataSetChanged()
    }

    inner class ChooseViewHolder(v: View) : BaseViewHolder(v, context) {
        private val binding: ItemChooseBinding? = DataBindingUtil.bind(v)
        init {
            v.tag = binding
        }

        override fun binding(position: Int, data: DataType) {
            binding?.let {
                if (currentItem == position) {
                    it.imgChoose.setImageResource(data.iconActive())
                } else {
                    it.imgChoose.setImageResource(data.icon())
                }
            }
        }
    }
}

