package com.zy.jet.notebook.ui.fragment

import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.FragmentSettingsBinding

/**
 * 设置界面
 */
class SettingsFragment : AbsSubMainUiFragment<FragmentSettingsBinding>() {
    override fun injectDataBinding() {
        super.injectDataBinding()
    }

    override fun getContentResId(): Int = R.layout.fragment_settings
}