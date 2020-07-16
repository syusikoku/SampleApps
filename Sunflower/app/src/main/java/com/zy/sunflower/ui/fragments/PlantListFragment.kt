package com.zy.sunflower.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import cn.charles.kasa.framework.utils.LogUtil
import cn.charles.kasa.framework.utils.Utils
import com.zy.sunflower.R
import com.zy.sunflower.adapters.NewPlantListAdapter
import com.zy.sunflower.adapters.PlantListAdapter
import com.zy.sunflower.databinding.FragmentPlantListBinding
import com.zy.sunflower.repository.local.room.entry.Plant
import com.zy.sunflower.utils.InjectorUtils
import com.zy.sunflower.viewmodel.PlantsListViewModel

/**
 * 植物列表
 */
class PlantListFragment : BaseDataBindingFragment<FragmentPlantListBinding>() {
    // 创建viewModel实例
    private val viewModel: PlantsListViewModel by viewModels {
        InjectorUtils.providePlantListViewModelFactory(this)
    }

    override fun injectDataBinding() {
    }

    override fun getContentResId(): Int = R.layout.fragment_plant_list

    override fun initView() {
        // 显示菜单
        setHasOptionsMenu(true)
    }

    override fun initData() {
        // var list: ArrayList<Plant> = ArrayList<Plant>()
        // var plant: Plant? = null
        // buildTestData(plant, list)
//        val plantListAdapter = PlantListAdapter(mCtx, ArrayList<Plant>())
        val plantListAdapter = NewPlantListAdapter()
        mBinding.rvPlants.adapter = plantListAdapter
        subscribeUi2(plantListAdapter)
    }

    /**
     * 订阅Ui
     */
    private fun subscribeUi(adapter: PlantListAdapter) {
        viewModel.plants.observe(viewLifecycleOwner) { datalist ->
            LogUtil.loge("subscribeUi done ")
            adapter.setData(datalist)
        }
    }

    /**
     * 订阅Ui
     */
    private fun subscribeUi2(adapter: NewPlantListAdapter) {
        viewModel.plants.observe(viewLifecycleOwner) { datalist ->
            LogUtil.loge("subscribeUi done ")
            adapter.submitList(datalist)
        }
    }

    private fun buildTestData(
        plant: Plant?,
        list: ArrayList<Plant>
    ) {
        var plant1 = plant
        for (index in 1..10) {
            plant1 = Plant(
                "test$index",
                "test",
                index,
                "f-$index",
                index,
                ""
            )
            list.add(plant1)
        }
    }

    /**
     * 创建菜单
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plantlist, menu)
    }

    /**
     * 菜单项被点击
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mid_filter_zone -> {
                Utils.showToastSafe("筛选")
                execFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * 过滤筛选
     */
    private fun execFilter() {
        with(viewModel) {
            if (isFiltered()) {
                clearGrowZoneNumber()
            } else {
                setGrowZoneNumber(9)
            }
        }
    }

}