package com.zy.jet.notebook.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import cn.charles.kasa.framework.base.BaseFragment
import cn.charles.kasa.kt.extentions.loge2
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentLocaldatasBinding
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.PlantViewModel
import com.zy.jet.notebook.ui.adapter.PlantListAdapter


/**
 * 本地数据：
 *   加载db中的plants数据
 */
class PlantListFragment : BaseDataBindingFragment<FragmentLocaldatasBinding>() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlantListAdapter

    private lateinit var viewModel: PlantViewModel

    override fun injectDataBinding() {
        viewModel = obtainViewModel(PlantViewModel::class.java)
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun getContentResId(): Int = R.layout.fragment_localdatas

    override fun initView() {
        recyclerView = mBinding.recyclerviewPlants
    }

    override fun initData() {
        adapter = PlantListAdapter(BaseFragment.mCtx)
        recyclerView.adapter = adapter
        viewModel.dataList.observe(this, Observer { list ->
            loge2("list = $list")
            adapter.setData(list)
        })
    }
}