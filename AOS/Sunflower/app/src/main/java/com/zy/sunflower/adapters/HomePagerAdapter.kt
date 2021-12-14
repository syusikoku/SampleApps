package com.zy.sunflower.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zy.sunflower.ui.fragments.GardenPlantsFragment
import com.zy.sunflower.ui.fragments.PlantListFragment

// 定义索引: 花园及植物列表
const val MY_GRADEN_PAGE_INDEX = 0
const val PLANT_LIST_PAGE_INDEX = 1

/**
 * 首页的适配器
 */
class HomePagerAdapter(fm: Fragment) : FragmentStateAdapter(fm) {
    // 定义映射关系： 索引与Fragment的对应关系
    // 构建Map<Int,Fragment>
    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        MY_GRADEN_PAGE_INDEX to { GardenPlantsFragment() },
        PLANT_LIST_PAGE_INDEX to { PlantListFragment() }
    )

    override fun getItemCount(): Int = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment =
        tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
}