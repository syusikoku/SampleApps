package com.zy.jet.notebook.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.MainViewModel

/**
 * 基础的MainActivity中的子界面
 */
abstract class AbsSubMainUiFragment<VT : ViewDataBinding> : BaseDataBindingFragment<VT>() {
    // 获取activity中的viewmodel,起到viewmodel共用的效果
    lateinit var mainViewModel: MainViewModel
    lateinit var activity: AppCompatActivity

    override fun injectDataBinding() {
        activity = mAct as AppCompatActivity
        // 复用activity中的viewmodel
        mainViewModel = activity.obtainViewModel(MainViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        mainViewModel?.setInMainUI(false)
    }

    override fun onPause() {
        super.onPause()
        mainViewModel?.setInMainUI(true)
    }
}