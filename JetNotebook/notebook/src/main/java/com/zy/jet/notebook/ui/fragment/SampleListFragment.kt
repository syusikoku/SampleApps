package com.zy.jet.notebook.ui.fragment

import android.content.Context
import androidx.lifecycle.Observer
import cn.charles.kasa.framework.adapter.BaseBindingViewHolder
import cn.charles.kasa.framework.adapter.BaseSampleListAdapter
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import cn.charles.kasa.framework.base.BaseFragment
import com.zy.jet.notebook.R
import com.zy.jet.notebook.bean.DataBean
import com.zy.jet.notebook.databinding.FragmentSampleListBinding
import com.zy.jet.notebook.databinding.ItemHomelistBinding
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.SampleListViewModel

/**
 * 简单示例列表
 */
class SampleListFragment : BaseDataBindingFragment<FragmentSampleListBinding>() {
    private lateinit var viewModel: SampleListViewModel

    private lateinit var adapter: MyAdapter

    override fun getContentResId(): Int = R.layout.fragment_sample_list

    override fun injectDataBinding() {
        viewModel = obtainViewModel(SampleListViewModel::class.java)
    }

    override fun initView() {
        adapter = MyAdapter(BaseFragment.mCtx)
        mBinding.recyclerview.adapter = adapter
    }

    override fun initData() {
        viewModel.dataList.observe(viewLifecycleOwner, Observer { list ->
            list.let {
                adapter.setData(list)
            }
        })
    }

    class MyAdapter(ctx: Context) : BaseSampleListAdapter<DataBean, ItemHomelistBinding>(ctx) {
        override fun getLayoutId(p0: Int): Int = R.layout.item_homelist

        override fun bindData(
            p0: BaseBindingViewHolder<ItemHomelistBinding>?,
            p1: DataBean?,
            p2: Int
        ) {
            p0?.mBinding?.tvItemHomeTitle?.isSingleLine = false
            p0?.mBinding?.dataItem = p1
        }

    }

}