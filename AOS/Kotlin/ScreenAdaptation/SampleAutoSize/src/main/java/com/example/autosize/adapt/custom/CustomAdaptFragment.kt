package com.example.autosize.adapt.custom

import androidx.fragment.app.Fragment
import com.example.autosize.adapt.Appconst
import me.jessyan.autosize.internal.CustomAdapt


/**
 * 自定义适配的Fragment
 * 需要结合:
 *    AutoSizeConfig.getInstance().setCustomFragment(true);
 */
class CustomAdaptFragment:Fragment(), CustomAdapt {
    override fun isBaseOnWidth(): Boolean =false

    // 这是iphone 6的尺寸: 335 * 667
     override fun getSizeInDp(): Float = Appconst.IPHONE6_DESIGN_SIZE_HEIGHT

//    override fun getSizeInDp(): Float = 640.0f
}