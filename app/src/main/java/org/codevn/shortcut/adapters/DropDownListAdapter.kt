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
import org.codevn.shortcut.data.AppListMain
import org.codevn.shortcut.data.CameraChoose
import org.codevn.shortcut.databinding.ItemAdditionalOptionBinding


class DropDownListAdapter(
    private val context: Context,
    private val onClickItem: (Int, CameraChoose?, AppListMain?) -> Unit
) : RecyclerView.Adapter<BaseViewHolder>() {
    private var data = ArrayList<Any>()
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = layoutInflater.inflate(R.layout.item_additional_option, parent, false)
        return ChooseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is ChooseViewHolder -> {
                when (data[position]) {
                    is CameraChoose -> {
                        holder.binding(position, data[position] as CameraChoose)
                    }
                    is AppListMain -> {
                        holder.binding(position, data[position] as AppListMain)
                    }
                }

            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: List<Any>, isFirst: Boolean) {
        if (isFirst) {
            data.clear()
        }
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ChooseViewHolder(v: View) : BaseViewHolder(v, context) {
        private val binding: ItemAdditionalOptionBinding? = DataBindingUtil.bind(v)

        init {
            v.tag = binding
        }

        fun binding(position: Int, data: CameraChoose) {
            binding?.imageView?.setImageResource(data.icon)
            binding?.textView?.text = data.appName
            binding?.root?.setOnClickListener {
                onClickItem.invoke(position, data, null)
            }
        }

        fun binding(position: Int, data: AppListMain) {
            binding?.imageView?.setImageDrawable(data.icon)
            binding?.textView?.text = data.appName
            binding?.root?.setOnClickListener {
                onClickItem.invoke(position, null, data)
            }
        }
    }
}