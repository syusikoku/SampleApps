package com.zy.sunflower.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearancePathProvider
import com.zy.sunflower.R

/**
 * 自定义cardview
 */
class MaskedCardView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyle) {
    @SuppressLint("RestrictedApi")
    // 路径提供者
    private val pathProvider = ShapeAppearancePathProvider()

    // 定义路径
    private val path: Path = Path()
    // 矩形对象
    private val rectF = RectF(0f, 0f, 0f, 0f)

    // 动态设置图形的形状
    private val shapeAppearance: ShapeAppearanceModel = ShapeAppearanceModel.builder(
        context,
        attrs,
        defStyle,
        R.style.Widget_MaterialComponents_CardView
    ).build()

    override fun onDraw(canvas: Canvas?) {
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rectF.right = w.toFloat();
        rectF.bottom = h.toFloat();
        pathProvider.calculatePath(shapeAppearance, 1f, rectF, path)
        super.onSizeChanged(w, h, oldw, oldh)
    }
}