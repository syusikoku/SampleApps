package com.zy.sunflower.adapters

import android.content.Context
import androidx.navigation.findNavController
import cn.charles.kasa.framework.adapter.BaseBindingViewHolder
import cn.charles.kasa.framework.adapter.BaseSampleListAdapter
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.R
import com.zy.sunflower.databinding.LayoutItemGardenPlantsBinding
import com.zy.sunflower.repository.local.room.entry.PlantAndGardenPlant
import com.zy.sunflower.ui.fragments.HomePageFragmentDirections
import com.zy.sunflower.viewmodel.GardenAndPlantsViewModel

/**
 *  花园植物
 */
class GardenPlantListAdapter(ctx: Context) :
    BaseSampleListAdapter<PlantAndGardenPlant, LayoutItemGardenPlantsBinding>(ctx, ArrayList()) {
    override fun getLayoutId(p0: Int): Int = R.layout.layout_item_garden_plants

    override fun bindData(
        p0: BaseBindingViewHolder<LayoutItemGardenPlantsBinding>?,
        p1: PlantAndGardenPlant?,
        p2: Int
    ) {
        LogUtil.loge("绑定viewmodel")
        val gardenAndPlantsViewModel = GardenAndPlantsViewModel(p1!!)
        p0?.mBinding?.viewmodel = gardenAndPlantsViewModel
        p0?.mBinding?.setItemClickListener { view ->
            val dest = HomePageFragmentDirections.actionHomePageFragmentToPlantDetailFragment(
                gardenAndPlantsViewModel.plantId
            )
            view.findNavController().navigate(dest)
        }
        p0?.mBinding?.executePendingBindings()
    }

}