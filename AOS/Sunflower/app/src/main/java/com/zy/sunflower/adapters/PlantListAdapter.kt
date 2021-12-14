package com.zy.sunflower.adapters

import android.content.Context
import android.view.View
import androidx.navigation.findNavController
import cn.charles.kasa.framework.adapter.BaseBindingViewHolder
import cn.charles.kasa.framework.adapter.BaseSampleListAdapter
import cn.charles.kasa.framework.utils.Utils
import com.zy.sunflower.R
import com.zy.sunflower.databinding.LayoutItemPlantsBinding
import com.zy.sunflower.repository.local.room.entry.Plant
import com.zy.sunflower.ui.fragments.HomePageFragmentDirections


/**
 * 植物列表适配器
 */
class PlantListAdapter(ctx: Context, list: List<Plant>) :
    BaseSampleListAdapter<Plant, LayoutItemPlantsBinding>(ctx, list) {
    override fun getLayoutId(p0: Int): Int = R.layout.layout_item_plants

    override fun bindData(
        p0: BaseBindingViewHolder<LayoutItemPlantsBinding>?,
        p1: Plant?,
        p2: Int
    ) {
        p0?.mBinding?.item = p1
        p0?.mBinding?.setItemClickListener {
            // 进入到详情
            Utils.showToastSafe("进入到详情")
            navigationToPlant(p1!!, it)
        }

    }

    /**
     * 进入到详情
     */
    private fun navigationToPlant(plant: Plant, view: View?) {
        Utils.showToastSafe("navigationToPlant")
        val direction =
            HomePageFragmentDirections.actionHomePageFragmentToPlantDetailFragment(plant.plantId)
        view?.findNavController()?.navigate(direction)
    }

}