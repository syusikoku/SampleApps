package com.example.percentsupport

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

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