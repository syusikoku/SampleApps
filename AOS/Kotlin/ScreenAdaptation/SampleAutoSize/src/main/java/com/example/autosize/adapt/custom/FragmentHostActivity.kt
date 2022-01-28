package com.example.autosize.adapt.custom

import com.example.autosize.R
import com.example.autosize.databinding.ActivityFragmentHostBinding
import com.example.mybase.BaseDatabindAct
import me.jessyan.autosize.internal.CustomAdapt


/**
 *  以宽度720进行的自定义适配
 */
class FragmentHostActivity : BaseDatabindAct<ActivityFragmentHostBinding>(), CustomAdapt {


    override fun getLayoutResId(): Int = R.layout.activity_fragment_host

    override fun initView() {
        if (supportFragmentManager.findFragmentById(R.id.container1) == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container1, CustomFragment1()).commit()
        }
        if (supportFragmentManager.findFragmentById(R.id.container2) == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container2, CustomFragment2()).commit()
        }
        if (supportFragmentManager.findFragmentById(R.id.container3) == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container3, CustomFragment3()).commit()
        }
    }

    override fun isBaseOnWidth(): Boolean = true

    override fun getSizeInDp(): Float = 720.0f
}