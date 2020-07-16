package com.zy.jet.notebook.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zy.jet.notebook.ui.fragment.GankGirlsFragment
import com.zy.jet.notebook.ui.fragment.GankGirlsFragment2
import com.zy.jet.notebook.ui.fragment.PlantListFragment

const val INDEX_LOCAL = 0
const val INDEX_REMOTE = 1
const val INDEX_REMOTE2 = 2

/**
 * LiveData 示例模块的适配器
 */
class LiveDataPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {

    val pageIndicators: Map<Int, () -> Fragment> = mapOf(
        INDEX_LOCAL to { PlantListFragment() },
        INDEX_REMOTE to { GankGirlsFragment() },
        INDEX_REMOTE2 to { GankGirlsFragment2() }
    )

    override fun getItemCount(): Int = pageIndicators.size

    override fun createFragment(position: Int): Fragment =
        pageIndicators[position]?.invoke() ?: throw RuntimeException()

}