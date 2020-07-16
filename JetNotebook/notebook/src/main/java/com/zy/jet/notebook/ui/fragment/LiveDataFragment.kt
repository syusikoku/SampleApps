package com.zy.jet.notebook.ui.fragment

import com.google.android.material.tabs.TabLayoutMediator
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentLivedatasBinding
import com.zy.jet.notebook.ui.adapter.INDEX_LOCAL
import com.zy.jet.notebook.ui.adapter.INDEX_REMOTE
import com.zy.jet.notebook.ui.adapter.INDEX_REMOTE2
import com.zy.jet.notebook.ui.adapter.LiveDataPagerAdapter

/**
 * livedata fragment
 */
class LiveDataFragment : AbsSubMainUiFragment<FragmentLivedatasBinding>() {

    override fun injectDataBinding() {
        super.injectDataBinding()
    }

    override fun getContentResId(): Int = R.layout.fragment_livedatas

    override fun initView() {
        mBinding.vpLd.adapter = LiveDataPagerAdapter(this)
        // 绑定TabLayout和ViewPager2
        TabLayoutMediator(mBinding.tlLd, mBinding.vpLd) { tabItem, position ->
            tabItem.text = getTabTitle(position)
        }.attach()
    }

    // 获取标题
    private fun getTabTitle(position: Int): CharSequence? {
        return when (position) {
            INDEX_LOCAL -> "本地数据"
            INDEX_REMOTE -> "网络数据"
            INDEX_REMOTE2 -> "网络数据2"
            else -> null
        }
    }

}
