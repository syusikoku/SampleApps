package com.zy.sunflower.ui.fragments

import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import com.zy.sunflower.R
import com.zy.sunflower.adapters.NewGardenPlantListAdapter2
import com.zy.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.zy.sunflower.databinding.FragmentGardentPlantsBinding
import com.zy.sunflower.utils.InjectorUtils
import com.zy.sunflower.viewmodel.GardenPlantViewModel

/**
 * 花园植物
 */
class GardenPlantsFragment : BaseDataBindingFragment<FragmentGardentPlantsBinding>() {
    private val viewModel: GardenPlantViewModel by viewModels {
        InjectorUtils.provideGardenPlantViewModelFactory(this)
    }

    //    lateinit var gardenPlantListAdapter: GardenPlantListAdapter
    lateinit var gardenPlantListAdapter: NewGardenPlantListAdapter2

    override fun injectDataBinding() {
    }

    override fun getContentResId(): Int = R.layout.fragment_gardent_plants

    override fun initView() {
//        gardenPlantListAdapter = GardenPlantListAdapter(requireContext())
        gardenPlantListAdapter = NewGardenPlantListAdapter2()
        mBinding.rvGardenPlants.adapter = gardenPlantListAdapter
    }

    override fun addListener() {
        mBinding.addPlant.setOnClickListener { view ->
            navigateToPlantListPage()
        }
    }

    /**
     * 切换到植物界面
     */
    private fun navigateToPlantListPage() {
        // 切换viewpager
        requireActivity().findViewById<ViewPager2>(R.id.viewpager).currentItem =
            PLANT_LIST_PAGE_INDEX
    }

    override fun initData() {
        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            e("plants size : ${plants?.size}")
            // 填充数据
            mBinding.hasPlantings = !plants.isNullOrEmpty()
//            gardenPlantListAdapter.setData(plants)
            gardenPlantListAdapter.submitList(plants)
        }
    }

}