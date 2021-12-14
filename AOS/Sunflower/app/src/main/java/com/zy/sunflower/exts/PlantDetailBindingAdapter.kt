package com.zy.sunflower.exts

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zy.sunflower.R

/**
 * imageview绑定url使用
 */
@BindingAdapter("imgFromUrl")
fun bindImageFromUrl(view: ImageView, imgUrl: String?) {
    if (!imgUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imgUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("wateringText")
fun bindWaterTxt(view: TextView, wateringInterVal: Int) {
    val resources = view.context.resources
    val quantityStr = resources.getQuantityString(
        R.plurals.watering_needs_suffix,
        wateringInterVal,
        wateringInterVal
    )
    view.text = quantityStr
}

@BindingAdapter("renderHtml")
fun bindRenderHtml(view: TextView, des: String?) {
    if (des != null) {
        view.text = HtmlCompat.fromHtml(des, HtmlCompat.FROM_HTML_MODE_COMPACT)
        view.movementMethod = LinkMovementMethod.getInstance()
    } else {
        view.text = ""
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: FloatingActionButton, isGone: Boolean) {
    if (isGone == null || isGone) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}