package com.zy.jet.notebook.jetpack.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.zy.jet.notebook.ui.fragment.NavigationFragmentDirections


/**
 * 界面跳转：
 *   三种方式都可以
 *   layout: 事件绑定需要用如下的方式
 *    android:onClick="@{(view)-> viewModel.goBottomNavSample(view)}"
 */
class NavFragmentViewModel() : ViewModel() {
    fun goNavSample(v: View) {
        //  act.forward(NavSampleActivity::class.java)
        // v.findNavController().navigate(R.id.navSampleActivity)
        val directions = NavigationFragmentDirections.navToNewSample()
        v.findNavController().navigate(directions)
    }

    fun goNavSample2(v: View) {
        // act.forward(NavSampleActivity2::class.java)
        // v.findNavController().navigate(R.id.navSampleActivity2)

        val directions = NavigationFragmentDirections.navToNewSample2()
        v.findNavController().navigate(directions)
    }

    fun goBottomNavSample(v: View) {
        //  act.forward(BottomNavSampleActivity::class.java)
        // v.findNavController().navigate(R.id.bottomNavSampleActivity)

        val directions = NavigationFragmentDirections.navToNewBsample()
        v.findNavController().navigate(directions)
    }
}