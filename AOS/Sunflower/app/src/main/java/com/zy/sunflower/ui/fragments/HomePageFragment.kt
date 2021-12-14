package com.zy.sunflower.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import cn.charles.kasa.framework.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zy.sunflower.R
import com.zy.sunflower.adapters.HomePagerAdapter
import com.zy.sunflower.adapters.MY_GRADEN_PAGE_INDEX
import com.zy.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.zy.sunflower.databinding.FragmentHomepagerBinding

/**
 * 主页
 */
class HomePageFragment : BaseFragment() {
    private lateinit var binding: FragmentHomepagerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomepagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getContentResId(): Int = 0

    override fun initData() {
        // 设置适配器
        binding.viewpager.adapter = HomePagerAdapter(this)
        // 设置图标和文字给每个tab: tablayout
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        // 设置toolbar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    /**
     * 获取图标
     */
    private fun getTabIcon(position: Int): Int {
        return when (position) {
            MY_GRADEN_PAGE_INDEX -> R.drawable.garden_tab_selector
            PLANT_LIST_PAGE_INDEX -> R.drawable.plant_list_tab_selector
            else -> throw IndexOutOfBoundsException()
        }
    }

    /**
     * 获取标题
     */
    private fun getTabTitle(position: Int): CharSequence? {
        return when (position) {
            MY_GRADEN_PAGE_INDEX -> getString(R.string.my_garden_title)
            PLANT_LIST_PAGE_INDEX -> getString(R.string.plant_list_title)
            else -> null
        }
    }
}
