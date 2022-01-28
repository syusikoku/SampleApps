package com.example.autosize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.autosize.adapt.custom.CustomAdaptActivity
import com.example.autosize.databinding.ActivityMainBinding
import com.example.mybase.BaseDatabindAct
import me.jessyan.autosize.AutoSizeConfig


/**
 * 本框架的核心原理来自于： <a href="https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA">今日头条官方适配方案</a>
 * 此方案不光可以适配 Activity, 此Activity下所有的Fragment,Dialog,View 都会自动适配
 *
 * 本Activity是以屏幕宽度为基准进行适配的，并且使用的是在AndroidManifest中填写的全局设计图尺寸360*640,
 *
 * AndroidAutoSize默认僵尸以屏幕宽度为基准进行适配，如果想更换为全局以屏幕高度进行适配，需要在BaseApplication中按注释中理性，为什么强调全局?
 *
 * 因为AndroidAutoSize 允许每个Activity 可以自定义适配参数，自定义适配参数通过实现CustomAdapt
 *
 * 如果不自定义适配参数就会使用全局的适配参数，全局适配参数在BaseApplication中按注释配置
 */
//实现 CancelAdapt 即可取消当前 Activity 的屏幕适配, 并且这个 Activity 下的所有 Fragment 和 View 都会被取消适配
//public class MainActivity extends AppCompatActivity implements CancelAdapt {
class MainActivity : BaseDatabindAct<ActivityMainBinding>() {


    override fun getLayoutResId(): Int = R.layout.activity_main


    override fun initBinding() {
        binding.clickPresenter = ClickPresenter()
    }


    inner class ClickPresenter {
        /**
         * 需要注意的是暂停 AndroidAutoSize 后, AndroidAutoSize 只是停止了对后续还没有启动的 {@link Activity} 进行适配的工作
         * 但对已经启动且已经适配的 {@link Activity} 不会有任何影响
         */
        fun execStopAction() {
            printLog("ClickPresenter execStopAction")
            AutoSizeConfig.getInstance().stop(this@MainActivity)
        }

        /**
         * 需要注意的是重新启动 AndroidAutoSize 后, AndroidAutoSize 只是重新开始了对后续还没有启动的 {@link Activity} 进行适配的工作
         * 但对已经启动且在 stop 期间未适配的 {@link Activity} 不会有任何影响
         */
        fun execRestartAction() {
            printLog("ClickPresenter execRestartAction")
            AutoSizeConfig.getInstance().restart()
        }

        fun execCustomAdaptAction() {
            printLog("ClickPresenter doCustomAdaptßßAction")
            forward(CustomAdaptActivity::class.java)
        }
    }
}