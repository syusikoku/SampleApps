package com.zy.jet.notebook.ui.activity.samples

import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.charles.kasa.framework.base.BaseDataBindingActivity
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.ActivityNavSampleBinding

/**
 * navigation 示例
 */
class NavSampleActivity : BaseDataBindingActivity<ActivityNavSampleBinding>() {
    private lateinit var navController: NavController

    override fun getLayoutResId(): Int = R.layout.activity_nav_sample

    override fun initView() {
        mBinding.toolbarNavs.title = javaClass.simpleName
        setSupportActionBar(mBinding.toolbarNavs)

        navController = Navigation.findNavController(this, R.id.fragment_navs)
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navsContentFragment), mBinding.drawerlayoutNavs)
        setupActionBarWithNavController(navController, appBarConfiguration)
        mBinding.navNavsSample.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mBinding.drawerlayoutNavs.openDrawer(GravityCompat.START)
            }
        }
        return true
    }

}