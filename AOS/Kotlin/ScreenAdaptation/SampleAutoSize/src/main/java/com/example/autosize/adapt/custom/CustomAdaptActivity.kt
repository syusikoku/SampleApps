package com.example.autosize.adapt.custom

import com.example.autosize.R
import com.example.autosize.adapt.Appconst
import com.example.autosize.databinding.CustomAdaptActivityBinding
import com.example.mybase.BaseDatabindAct
import me.jessyan.autosize.internal.CustomAdapt


/**
 * 自定义以Iphon6 高度667 适配的Activity
 *
 * 展示项目内部的Activity 自定义适配参数的用法，需要实现CustomAdapt
 * 现在AndroidAutoSize 是全局以屏幕宽度为基准进行适配，并且全局的设计图尺寸为360*640
 * 这里就展示怎么让当前这个界面，有别于全局设置，以屏幕高度为基准进行适配，并且更改设计图尺寸为
 * iPhone的设计图尺寸，如果这个页面设计图尺寸有别于其它界面，AndroidAutoSize 允许您改变单个
 * 页面的设计图尺寸 getSizeInDp
 *
 */
class CustomAdaptActivity : BaseDatabindAct<CustomAdaptActivityBinding>(), CustomAdapt {
    override fun isBaseOnWidth(): Boolean = false

    // 这是iphone 6的尺寸: 335 * 667
    override fun getSizeInDp(): Float = Appconst.IPHONE6_DESIGN_SIZE_HEIGHT

    override fun getLayoutResId(): Int = R.layout.custom_adapt_activity

    override fun initView() {
        setTitle("自定义Iphone6高度适配")
    }

    override fun initBinding() {
        binding.clickPresenter = CustomAdaptClickPresenter()
    }

    inner class CustomAdaptClickPresenter {
        fun execShowFragmentAdapt() {
            printLog("CustomAdaptClickPresenter execShowFragmentAdapt")
            forward(FragmentHostActivity::class.java)
        }
    }
//    override fun getSizeInDp(): Float = 640.0f
}