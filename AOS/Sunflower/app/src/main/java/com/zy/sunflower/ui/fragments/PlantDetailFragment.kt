package com.zy.sunflower.ui.fragments

import android.content.Intent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ShareCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import cn.charles.kasa.framework.base.BaseDataBindingFragment
import cn.charles.kasa.framework.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.zy.sunflower.R
import com.zy.sunflower.databinding.FragmentPlantDetailsBinding
import com.zy.sunflower.repository.local.room.entry.Plant
import com.zy.sunflower.utils.InjectorUtils
import com.zy.sunflower.viewmodel.PlantDetailViewModel

/**
 * 植物详情
 */
class PlantDetailFragment : BaseDataBindingFragment<FragmentPlantDetailsBinding>() {

    override fun getContentResId(): Int = R.layout.fragment_plant_details

    // 参数： 主要是取植物的id: plant_id
    private val args: PlantDetailFragmentArgs by navArgs()

    private val plantDetailViewModel: PlantDetailViewModel by viewModels {
        e(" PlantDetailViewModel by viewModels")
        InjectorUtils.providePlantDetailViewModelFactory(requireActivity(), args.plantId)
    }

    override fun injectDataBinding() {
        mBinding.apply {
            e(" binding.apply")
            // TODO 这一行要添加，要不然LiveData数据不会显示
            lifecycleOwner = viewLifecycleOwner
            //        // 设置数据
            videmodel = plantDetailViewModel
            callback = object : Callback {
                override fun add(plant: Plant) {
                    plant?.let {
                        e(" hideAppBarTab")
                        hideAppBarTab(fab)
                        e(" addPlantToGarden")
                        plantDetailViewModel.addPlantToGarden()
                        Snackbar.make(
                            mBinding.root,
                            R.string.added_plant_to_garden,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }
    }

    /**
     * 隐藏appbar
     */
    private fun hideAppBarTab(fab: FloatingActionButton) {
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior
        behavior.isAutoHideEnabled = false
        fab.hide()
    }

    override fun initView() {
        e(" initView")
        setHasOptionsMenu(true)
    }

    override fun addListener() {
        mBinding.toolbar.setNavigationOnClickListener { view ->
            // 返回
            view.findNavController().navigateUp()
        }

        // 菜单条目点击事件
        mBinding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.mid_share -> {
                    Utils.showToastSafe("分享")
                    createShareIntent()
                    true
                }
                else -> false
            }
        }

        // toolbar是否显示
        var isToolbarShown = false

        // scrollview滑动事件: y = 0时，图片完全关闭
        /**
         * scrollY: 0 , toolbar.height: 84 图片完全可见,使用内容的标题，toolbar title 不可用
         *
         * scrollY: 110 , toolbar.height: 84 图片不可见，使用toolabar,toolbar title可用
         */
        mBinding.nsvPlantDetail.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                e("scrollY: $scrollY , toolbar.height: ${mBinding.toolbar.height}")
                val shouldShowToolbar = scrollY > mBinding.toolbar.height
                if (isToolbarShown != shouldShowToolbar) {
                    isToolbarShown = shouldShowToolbar
                    mBinding.appBarLayout.isActivated = shouldShowToolbar
                    mBinding.toolbarLayout.isTitleEnabled = shouldShowToolbar
                }
            })
    }

    override fun initData() {
//        plantDetailViewModel.isPlanted.observe(viewLifecycleOwner) { it ->
//            e("isPlanted $it")
//        }
    }

    /**
     * 分享
     */
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
        e("createShareIntent")
        plantDetailViewModel.plant.value.let { plant ->
            val shareTxt = if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_plant, plant.name)
            }
            e("shareTxt :$shareTxt")
            val shareIntent = ShareCompat.IntentBuilder.from(activity!!)
                .setText(shareTxt)
                .setType("text/plain")
                .createChooserIntent()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_NEW_TASK)
            e("do share...")
            startActivity(shareIntent)
        }
    }

    interface Callback {
        /**
         * 添加植物
         */
        fun add(plant: Plant)
    }


}