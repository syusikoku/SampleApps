package com.zy.jet.notebook.ui.adapter

import android.content.Context
import androidx.navigation.findNavController
import cn.charles.kasa.framework.adapter.BaseBindingViewHolder
import cn.charles.kasa.framework.adapter.BaseSampleListAdapter
import com.zy.jet.notebook.R
import com.zy.jet.notebook.bean.DataBean
import com.zy.jet.notebook.databinding.ItemHomelistBinding
import com.zy.jet.notebook.ui.fragment.HomeFragmentDirections

/**
 * HomeFragment 界面列表adapter
 */
class HomeListAdapter(ctx: Context) :
    BaseSampleListAdapter<DataBean, ItemHomelistBinding>(ctx) {
    override fun getLayoutId(p0: Int): Int = R.layout.item_homelist

    override fun bindData(
        p0: BaseBindingViewHolder<ItemHomelistBinding>?,
        p1: DataBean?,
        p2: Int
    ) {
        p0?.mBinding?.dataItem = p1
        p0?.mBinding?.itemHomeCard?.setOnClickListener { view ->
            // 以前的方式
            // val bundle = Bundle()
            // bundle.putInt("index", p2)
            // view.findNavController().navigate(R.id.action_homeFragment_to_webViewFragment, bundle)
            // 现在的方式
            // 进行界面的跳转
            val direction = HomeFragmentDirections.navToWebView(p2)
            view.findNavController().navigate(direction)
        }
        p0?.mBinding?.executePendingBindings()

    }

}