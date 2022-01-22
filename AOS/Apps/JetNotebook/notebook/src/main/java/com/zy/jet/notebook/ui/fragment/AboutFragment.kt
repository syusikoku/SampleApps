package com.zy.jet.notebook.ui.fragment

import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentAboutBinding


/**
 * 关于fragment
 */
class AboutFragment : AbsSubMainUiFragment<FragmentAboutBinding>() {
    override fun getContentResId(): Int = R.layout.fragment_about

    override fun injectDataBinding() {
        super.injectDataBinding()
    }
}