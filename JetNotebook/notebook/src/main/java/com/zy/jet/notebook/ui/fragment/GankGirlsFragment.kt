package com.zy.jet.notebook.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import cn.charles.kasa.framework.base.BaseFragment
import cn.charles.kasa.kt.extentions.loge
import cn.charles.kasa.kt.extentions.loge2
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentRemotedatasBinding
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.GankViewModel
import com.zy.jet.notebook.ui.adapter.ImgListAdapter
import com.zy.jet.notebook.ui.weight.layoutmanager.PhotoLayoutManager


/**
 * 网络数据
 * 加载Gank api Girls列表
 */
class GankGirlsFragment : BaseDataBindingFragment<FragmentRemotedatasBinding>() {
    private lateinit var imgListAdapter: ImgListAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: GankViewModel

    override fun injectDataBinding() {
        viewModel = obtainViewModel(GankViewModel::class.java)
        loge("viewModel2 $viewModel")
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
        }
    }

    override fun getContentResId(): Int = R.layout.fragment_remotedatas

    override fun initView() {
        recyclerView = mBinding.recyclerviewGirls
        imgListAdapter = ImgListAdapter()
        recyclerView.layoutManager = PhotoLayoutManager(imgListAdapter, BaseFragment.mCtx, 2)

        recyclerView.adapter = imgListAdapter
    }

    override fun initData() {
        viewModel.gankList.observe(this, Observer {
            loge2("it = $it")
            imgListAdapter.submitList(it)
        })
    }


}