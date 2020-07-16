package com.zy.jet.notebook.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import cn.charles.kasa.framework.adapter.BaseBindingViewHolder
import com.zy.jet.notebook.R
import com.zy.jet.notebook.bean.Girl
import com.zy.jet.notebook.databinding.ItemGirls2Binding


/**
 * 图片列表适配器
 */
public class ImgListAdapter2() :
    PagedListAdapter<Girl, BaseBindingViewHolder<ItemGirls2Binding>>(diffCallback) {

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ItemGirls2Binding> {
        val binding: ItemGirls2Binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_girls2,
            parent,
            false
        )
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ItemGirls2Binding>, position: Int) {
        holder.mBinding.girlInfo = getItem(position)
    }
}
