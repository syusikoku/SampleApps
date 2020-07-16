package com.zy.jet.notebook.ui.adapter

import android.content.Context
import cn.charles.kasa.framework.adapter.BaseBindingViewHolder
import cn.charles.kasa.framework.adapter.BaseSampleListAdapter
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.ItemPlantListBinding
import com.zy.jet.notebook.jetpack.room.entry.Plant

/**
 * 植物列表
 */
class PlantListAdapter(context: Context) :
    BaseSampleListAdapter<Plant, ItemPlantListBinding>(context) {
    override fun getLayoutId(p0: Int): Int = R.layout.item_plant_list

    override fun bindData(p0: BaseBindingViewHolder<ItemPlantListBinding>?, p1: Plant?, p2: Int) {
        p0?.mBinding?.item = p1
    }
}