package com.example.autosize.adapt.custom

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.jessyan.autosize.utils.AutoSizeUtils

class CustomFragment2 : BaseWidthAdaptFragment() {

    override fun createRootView(inflater: LayoutInflater): View? {
        var view: TextView = TextView(mContext)
        var lp: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            AutoSizeUtils.dp2px(mContext, 360.0f), ViewGroup
                .LayoutParams
                .MATCH_PARENT
        )
        view.layoutParams = lp
        view.text = "Fragment-2 \n View width = 360dp \n Total width = 720dp"
        view.textSize = 30.0f
        view.gravity = Gravity.CENTER
        view.setTextColor(Color.WHITE)
        view.setBackgroundColor(0xff00ff00.toInt())
        return view
    }

    override fun getSizeInDp(): Float = 720.0f
}