package com.example.autosize.adapt.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.internal.CustomAdapt

/**
 * 以宽度进行适配的Fragment 基类
 */
open abstract class BaseWidthAdaptFragment : Fragment(), CustomAdapt {
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun isBaseOnWidth(): Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //由于某些原因, 屏幕旋转后 Fragment 的重建, 会导致框架对 Fragment 的自定义适配参数失去效果
        //所以如果您的 Fragment 允许屏幕旋转, 则请在 onCreateView 手动调用一次 SampleAutoSize.autoConvertDensity()
        //如果您的 Fragment 不允许屏幕旋转, 则可以将下面调用 SampleAutoSize.autoConvertDensity() 的代码删除掉
        AutoSize.autoConvertDensity(activity, sizeInDp, true)

        return createRootView(inflater)
    }

    abstract fun createRootView(inflater: LayoutInflater): View?

}