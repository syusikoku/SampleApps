package com.zy.jet.notebook.ui.activity.samples

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import cn.charles.kasa.framework.base.BaseDataBindingActivity
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.ActivityBottomnavSampleBinding


/**
 * 底部导航 示例
 */
class BottomNavSampleActivity : BaseDataBindingActivity<ActivityBottomnavSampleBinding>() {

    override fun getLayoutResId(): Int = R.layout.activity_bottomnav_sample

    private lateinit var navController: NavController

    override fun initView() {
        setSupportActionBar(mBinding.toolbarBnavs)

        navController = Navigation.findNavController(this, R.id.fragment_bnavs)
        mBinding.bnavsSample.setupWithNavController(navController)

    }

}