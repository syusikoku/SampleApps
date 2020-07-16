package com.zy.sunflower.adapters

import androidx.recyclerview.widget.DiffUtil
import cn.charles.kasa.framework.adapter.BaseBindingViewHolder
import cn.charles.kasa.framework.adapter.BaseSampleDiffListAdapter
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.R
import com.zy.sunflower.databinding.LayoutItemGardenPlantsBinding
import com.zy.sunflower.repository.local.room.entry.PlantAndGardenPlant
import com.zy.sunflower.viewmodel.GardenAndPlantsViewModel

/**
 * 新的花园植物列表适配器: 自动比对参数
 *  测试框架
 */
class NewGardenPlantListAdapter2 :
    BaseSampleDiffListAdapter<PlantAndGardenPlant, LayoutItemGardenPlantsBinding>(
        GardenPlantDiffCallback()
    ) {
    /**
     * 判断是否是同一个对象的回调
     */
    private class GardenPlantDiffCallback : DiffUtil.ItemCallback<PlantAndGardenPlant>() {
        override fun areItemsTheSame(
            oldItem: PlantAndGardenPlant,
            newItem: PlantAndGardenPlant
        ): Boolean {
            return oldItem.plant.plantId == newItem.plant.plantId
        }

        override fun areContentsTheSame(
            oldItem: PlantAndGardenPlant,
            newItem: PlantAndGardenPlant
        ): Boolean {
            return oldItem.plant == newItem.plant
        }
    }

    override fun getLayoutId(p0: Int): Int = R.layout.layout_item_garden_plants

    override fun bindData(
        p0: BaseBindingViewHolder<LayoutItemGardenPlantsBinding>?,
        p1: PlantAndGardenPlant?,
        p2: Int
    ) {
        LogUtil.loge("before binding viewmodel")
        p0?.mBinding?.viewmodel = GardenAndPlantsViewModel(p1!!)
        LogUtil.loge("before executePendingBindings")
        p0?.mBinding?.executePendingBindings()
        LogUtil.loge("over executePendingBindings")
    }
}
