package com.zy.jet.notebook.ui.fragment.navs

import cn.charles.kasa.framework.base.BaseDataBindingFragment
import cn.charles.kasa.kt.extentions.showSnackBar
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentNavsContentBinding

class NavsContentFragment : BaseDataBindingFragment<FragmentNavsContentBinding>() {
    override fun injectDataBinding() {
    }

    override fun getContentResId(): Int = R.layout.fragment_navs_content

    override fun addListener() {
        mBinding.fabNavs.setOnClickListener {
            showSnackBar(mBinding.root, "退出当前界面")
            // 退出界面
            requireActivity().finish()
        }
    }
}