package com.zy.jet.notebook.ui.activity

import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.Observable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.charles.kasa.framework.base.BaseDataBindingActivity
import com.zy.jet.notebook.R
import com.zy.jet.notebook.databinding.ActivityMainBinding
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.MainViewModel
import com.zy.jet.notebook.ui.fragment.HomeFragmentDirections

/**
 * 主界面
 */
class MainActivity : BaseDataBindingActivity<ActivityMainBinding>() {

    //    private val viewMode: MainViewModel by viewModels {
//        InjectUtils.provideMainViewModelFactory()
//    }

    private lateinit var viewMode: MainViewModel

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var isInMainCallback: Observable.OnPropertyChangedCallback

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initDataBinding() {
        viewMode = obtainViewModel(MainViewModel::class.java)
        e("viewMode ins = $viewMode")
        mBinding.viewModel = viewMode
    }

    override fun initView() {
        drawerLayout = mBinding.drawerlayout

        navController = Navigation.findNavController(this, R.id.main_nav_fragment)
        setSupportActionBar(mBinding.mainHeaderview.toolbar)

        // 添加到appBarConfiguration的fragment,进入的时候，NavigationIcon不会发生改变
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        mBinding.navMenuMain.setupWithNavController(navController)
    }

    override fun addListener() {
        isInMainCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                e("状态变化了： 处理手势的打开和关闭")
                val inWeb = viewMode.isInMainUI.get()
                if (inWeb) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }
        }
        viewMode.isInMainUI.addOnPropertyChangedCallback(isInMainCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        e("onCreateOptionsMenu")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (viewMode.isInMainUI.get()) {
                    drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    navController.navigateUp()
                }
                true
            }
            R.id.action_go_settings -> {
                val direction = HomeFragmentDirections.navToSettings()
                navController.navigate(direction)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        viewMode.isInMainUI.removeOnPropertyChangedCallback(isInMainCallback)
        super.onDestroy()
    }
}
