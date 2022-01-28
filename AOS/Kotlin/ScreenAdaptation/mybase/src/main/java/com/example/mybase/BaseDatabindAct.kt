package com.example.mybase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


/**
 * 基础的databind基类ß
 */
open abstract class BaseDatabindAct<T : ViewDataBinding> : AppCompatActivity() {
    lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<T>(this, getLayoutResId())
        initBinding()
        initView()
        addListeners()
        initData()
        refreshUI()
    }

    open fun initBinding() {
    }

    open fun initView() {
    }

    open fun addListeners() {
    }

    open fun initData() {
    }

    open fun refreshUI() {
    }

    abstract fun getLayoutResId(): Int


    fun printLog(msg: String) {
        Log.e("test", msg)
    }


    fun forward(cls: Class<out AppCompatActivity?>?) {
        if (cls == null) {
            return
        }
        startActivity(Intent(this, cls))
    }
}