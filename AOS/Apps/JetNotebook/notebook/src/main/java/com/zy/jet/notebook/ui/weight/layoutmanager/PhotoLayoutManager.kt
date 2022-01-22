package com.zy.jet.notebook.ui.weight.layoutmanager

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.charles.kasa.kt.extentions.loge2
import com.zy.jet.notebook.ui.adapter.ImgListAdapter


/**
 *https://blog.csdn.net/xiaoluoli88/article/details/47612417
 *
 * https://www.jianshu.com/p/f0959f23412f
 */
class PhotoLayoutManager(
    val adapter: ImgListAdapter,
    ctx: Context,
    spantCount: Int
) :
    GridLayoutManager(ctx, spantCount) {
    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {

        //不能使用   View view = recycler.getViewForPosition(0);
        //measureChild(view, widthSpec, heightSpec);
        // int measuredHeight  view.getMeasuredHeight();  这个高度不准确

        try {
            if (adapter != null && adapter.getItemHeight() > 0) {
                val measureWidth = View.MeasureSpec.getSize(widthSpec)
                var line: Int = adapter.itemCount / spanCount

                if (adapter.itemCount % spanCount > 0) {
                    line++
                }

                // 图片pading: bottom + top
                val measureHeight = adapter.getItemHeight() * line + 10 + 10
                setMeasuredDimension(measureWidth, measureHeight)
                loge2("setMeasuredDimension...")
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec)
            }
        } catch (e: Exception) {
            super.onMeasure(recycler, state, widthSpec, heightSpec)
        }


    }
}