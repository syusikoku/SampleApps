package com.zy.jet.notebook.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zy.jet.notebook.R
import com.zy.jet.notebook.bean.Girl
import com.zy.jet.notebook.databinding.ItemGirlsBinding
import cn.charles.kasa.kt.extentions.loge2


/**
 * 图片列表适配器
 */
public class ImgListAdapter() :
    PagedListAdapter<Girl, ImgListAdapter.ImgViewHolder>(diffCallback) {
    private var itemHeight: Int = 0

    private lateinit var viewHolder: ImgViewHolder

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImgListAdapter.ImgViewHolder {
        val binding: ItemGirlsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_girls,
            parent,
            false
        )

        viewHolder = ImgListAdapter.ImgViewHolder(binding)
        return viewHolder
    }


    class ImgViewHolder(val binding: ItemGirlsBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemHeight: Int = 0

        init {
            val listener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    itemHeight = itemView.measuredHeight
                    return true
                }
            }
            itemView.viewTreeObserver.addOnPreDrawListener(listener)
        }
    }

    fun getItemHeight(): Int = viewHolder?.itemHeight

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Girl>() {
            // 是否是同一个
            override fun areItemsTheSame(oldItem: Girl, newItem: Girl): Boolean =
                oldItem._id == newItem._id

            // 内容是否相等
            override fun areContentsTheSame(oldItem: Girl, newItem: Girl): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        loge2("itemHeight = $itemHeight")
        holder.binding.girlInfo = getItem(position)
        itemHeight = holder.itemHeight
    }


}
