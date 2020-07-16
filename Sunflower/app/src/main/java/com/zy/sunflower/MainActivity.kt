package com.zy.sunflower

import cn.charles.kasa.framework.base.BaseDataBindingActivity
import com.zy.sunflower.databinding.ActivityMainBinding

/**
 * 主界面
 *  基于navigation的实现
 */
class MainActivity : BaseDataBindingActivity<ActivityMainBinding>() {

    override fun getLayoutResId(): Int = R.layout.activity_main
}
