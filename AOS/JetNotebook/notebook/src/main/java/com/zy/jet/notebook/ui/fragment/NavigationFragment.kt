package com.zy.jet.notebook.ui.fragment

import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentNavigationBinding
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.NavFragmentViewModel


/**
 * navigation-fragment
 */
class NavigationFragment : AbsSubMainUiFragment<FragmentNavigationBinding>() {
    private lateinit var navViewModel: NavFragmentViewModel

    override fun injectDataBinding() {
        super.injectDataBinding()
        navViewModel = obtainViewModel(NavFragmentViewModel::class.java)

        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = navViewModel
        }
    }

    override fun getContentResId(): Int = R.layout.fragment_navigation


}