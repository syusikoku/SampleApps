package com.zy.sunflower.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.R
import com.zy.sunflower.databinding.LayoutItemGardenPlantsBinding
import com.zy.sunflower.repository.local.room.entry.PlantAndGardenPlant
import com.zy.sunflower.viewmodel.GardenAndPlantsViewModel

/**
 * 新的花园植物列表适配器: 自动比对参数
 */
class NewGardenPlantListAdapter :
    ListAdapter<PlantAndGardenPlant, NewGardenPlantListAdapter.ViewHolder>(GardenPlantDiffCallback()) {

    class ViewHolder(private val binding: LayoutItemGardenPlantsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataInfo: PlantAndGardenPlant) {
            with(binding) {
                if (viewmodel == null) {
                    viewmodel = GardenAndPlantsViewModel(dataInfo)
                    LogUtil.loge("viewModel = $viewmodel")
                    executePendingBindings()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutItemGardenPlantsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_item_garden_plants,
            parent,
            false
        )
        val holder = ViewHolder(
            binding
        )
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


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

}
