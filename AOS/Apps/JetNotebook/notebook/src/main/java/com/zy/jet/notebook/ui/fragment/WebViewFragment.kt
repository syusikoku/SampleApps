package com.zy.jet.notebook.ui.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import cn.charles.kasa.kt.KtUtils
import cn.charles.kasa.kt.extentions.config
import com.zy.jet.notebook.R
import com.zy.jet.notebook.bean.DataBean
import com.zy.jet.notebook.databinding.FragmentWebviewBinding
import com.zy.jet.notebook.extentions.obtainViewModel
import com.zy.jet.notebook.jetpack.viewmodel.HomeListViewModel

/**
 * 网页浏览的Fragment
 */
class WebViewFragment : AbsSubMainUiFragment<FragmentWebviewBinding>() {

    private lateinit var dataInfo: DataBean

    // 参数
    private val args: WebViewFragmentArgs by navArgs()

    private lateinit var homeListViewModel: HomeListViewModel

    override fun injectDataBinding() {
        super.injectDataBinding()
        // 复用activity中的viewmodel
        homeListViewModel = activity.obtainViewModel(HomeListViewModel::class.java)
        e("mainViewModel = $mainViewModel")
        with(mBinding) {
            // 这一步必须要设置
            lifecycleOwner = viewLifecycleOwner
            mBinding.mainViewModel = mainViewModel
            mBinding.homeListViewModel = homeListViewModel
        }
    }

    override fun getContentResId(): Int = R.layout.fragment_webview

    override fun initView() {
        // 设置菜单 显示
        setHasOptionsMenu(true)
        homeListViewModel.showLoading()
        initWebview()
    }

    private fun initWebview() {
        mBinding.webview.config(
            { it ->
                mBinding.pbWebview.progress = it
            },
            {
                homeListViewModel.hideLoading()
                mBinding.webview.visibility = View.GONE
            },
            {
                mBinding.pbWebview.visibility = View.GONE
                mBinding.webview.visibility = View.VISIBLE
            })
    }

    override fun initData() {
        // 之前的方式
        // val index = arguments?.getInt("index")
        // 新的方式
        val index = args.index
        if (index != null) {
            homeListViewModel.homeListData.observe(viewLifecycleOwner, Observer { list ->
                val data = list[index]
                dataInfo = data
                updateUI()
            })
        }
    }

    /**
     * 更新ui
     */
    private fun updateUI() {
        mBinding.webview.loadUrl(dataInfo.url)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.chageTitle(dataInfo.title)
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.chageTitle("")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        e("onCreateOptionsMenu")
        // 先清除，要不然菜单上会出现activity中的菜单
        menu.clear()
        inflater.inflate(R.menu.menu_webview, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share_webpage -> {
                KtUtils.showSnackbar(mBinding.root, "分享网页")
                true
            }
            R.id.action_copy_weburl -> {
                KtUtils.showSnackbar(mBinding.root, "复制url")
                true
            }
            R.id.action_broswer_webpage -> {
                KtUtils.showSnackbar(mBinding.root, "浏览网页")
                true
            }
        }
        return true
    }
}