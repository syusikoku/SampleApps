package com.example.percentsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.percentsupport.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.clickPresnter = OnClickPresenter()
    }


    inner class OnClickPresenter {
        /**
         * Constraint 示例
         */
        fun goSampleConstraintLayouts() {
            printLog("MainActivity-OnClickPresenter goSampleConstraintLayouts")
        }

        /**
         * 简单适配
         */
        fun showSampleAdaptation() {
            printLog("MainActivity-OnClickPresenter showSampleAdaptation")
            forward(SampleAdaptationActivity::class.java)
        }

        /**
         * 复杂适配
         */
        fun showComplexAdaptation() {
            printLog("MainActivity-OnClickPresenter showComplexAdaptation")
            forward(ComplexadaptationActivity::class.java)
        }
    }
}