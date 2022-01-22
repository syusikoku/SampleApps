package com.zy.jet.notebook.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import cn.charles.kasa.framework.base.BaseFragment
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentHomeBinding
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.HomeListViewModel
import com.zy.jet.notebook.ui.adapter.HomeListAdapter


/**
 * 首页fragment
 */
class HomeFragment : BaseDataBindingFragment<FragmentHomeBinding>() {
    // 重构后
    private lateinit var viewModel: HomeListViewModel

    private lateinit var homeListAdapter: HomeListAdapter

    override fun injectDataBinding() {
        viewModel = obtainViewModel(HomeListViewModel::class.java)

        // 虽然不加也可以起作用还是加一下，好点
        with(mBinding) {
            mBinding.lifecycleOwner = viewLifecycleOwner
            mBinding.viewModel = viewModel
        }
    }

    override fun getContentResId(): Int = R.layout.fragment_home

    override fun initView() {
        homeListAdapter = HomeListAdapter(BaseFragment.mCtx)
        mBinding.recyclerview.adapter = homeListAdapter
    }

    override fun addListener() {
        mBinding.fab.setOnClickListener {
            val layoutManager = mBinding.recyclerview.layoutManager
            if (layoutManager is StaggeredGridLayoutManager) {
                mBinding.recyclerview.layoutManager = LinearLayoutManager(BaseFragment.mCtx)
            } else {
                mBinding.recyclerview.layoutManager = StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )
            }
        }
    }

    override fun initData() {
        viewModel?.homeListData?.observe(viewLifecycleOwner, Observer { list ->
            homeListAdapter.setData(list)
            mBinding.executePendingBindings()
        })
    }

}