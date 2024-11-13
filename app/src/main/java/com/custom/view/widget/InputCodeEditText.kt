package com.custom.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.custom.view.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min


@SuppressLint("SoonBlockedPrivateApi")
class InputCodeEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private var measureWidth = 0
    private var measureHeight = 0
    private var maxLength = 0
    private var content: String = ""
    private var TAG: String = InputCodeEditText::class.java.name
    private var mPaint: Paint = Paint()
    private var scope = CoroutineScope(Dispatchers.Main)

    init {
        mPaint.strokeWidth = 2f
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.color = resources.getColor(R.color.purple_500, null)
        maxLength = getMaxLengthFromAttrs()
        println("Maximum Length Set: $maxLength")
    }

    // 获取 XML 中设置的 maxLength 属性值
    private fun getMaxLengthFromAttrs(): Int {
        // 获取 maxLength 属性值
        val maxLength = getMaxLengthFilter()
        return maxLength
    }

    // 从 InputFilter 中提取 maxLength 的值
    private fun getMaxLengthFilter(): Int {
        val filters = filters
        for (filter in filters) {
            if (filter is InputFilter.LengthFilter) {
                return filter.max
            }
        }
        return Int.MAX_VALUE  // 如果没有设置 maxLength，则返回一个极大的值
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)
        Log.e(TAG, "onMeasure text:${getWidth()}   ${getHeight()} ")
        Log.e(TAG, "onMeasure w:${measureWidth} ")
        Log.e(TAG, "onMeasure h:${measureHeight} ")
        val width = calculateWidth(widthMode, measureWidth)
        val height = calculateHeight(heightMode, measureHeight)

        setMeasuredDimension(width, height)
    }


    private fun calculateWidth(mode: Int, size: Int): Int {
        // 根据MeasureSpec的模式计算宽度
        var width = 0
        when (mode) {   // 精确尺寸，直接使用size
            MeasureSpec.EXACTLY ->
                width = size
            // 最大尺寸，不能超过size
            MeasureSpec.AT_MOST ->
                width = min(size, desiredWidth())
            // 未指定尺寸，使用期望的尺寸
            MeasureSpec.UNSPECIFIED ->
                width = desiredWidth()
        }
        return width
    }

    private fun calculateHeight(mode: Int, size: Int): Int {
        // 根据MeasureSpec的模式计算宽度
        var height = 0
        when (mode) {
            MeasureSpec.EXACTLY ->             // 精确尺寸，直接使用size
                height = size

            MeasureSpec.AT_MOST ->             // 最大尺寸，不能超过size
                height = min(size, desiredHeight())

            MeasureSpec.UNSPECIFIED ->             // 未指定尺寸，使用期望的尺寸
                height = desiredHeight()
        }
        return height
    }

    private fun desiredWidth(): Int {
        return 90000
    }

    private fun desiredHeight(): Int {
        return 120
    }

    var bulibuli: Boolean = false
    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
        var lastCursorIndex = 30f
        drawBackground(canvas)
        mPaint.textSize = 90f
        var index = 0f
        mPaint.color = resources.getColor(R.color.black, null)
        for (i in 0 until text!!.length) {
            if (i == 0) {
                index = 30f
            } else {
                index = i * 120f + 30f
            }
            canvas.drawText(text!![i].toString(), index, 90f, mPaint)
        }
        for (i in 0 until text!!.length) {
            lastCursorIndex = (i + 1) * 120f + 30f
        }
//        val rect = RectF(lastCursorIndex, 0f, lastCursorIndex + 10f, 190f)
//        canvas.drawRoundRect(rect, 20f, 20f, mPaint)
        if (text!!.length >= maxLength) {
            return
        }
        if (bulibuli) {
            bulibuli = !bulibuli
            mPaint.color = resources.getColor(R.color.white, null)
            val rect = RectF(lastCursorIndex, 0f, lastCursorIndex + 10f, 190f)
            canvas.drawRoundRect(rect, 20f, 20f, mPaint)
        } else {
            bulibuli = !bulibuli
            mPaint.color = resources.getColor(R.color.black, null)
            val rect = RectF(lastCursorIndex, 0f, lastCursorIndex + 10f, 190f)
            canvas.drawRoundRect(rect, 20f, 20f, mPaint)
        }
    }


    private fun drawBackground(canvas: Canvas) {
        val top = 0
        mPaint.color = resources.getColor(R.color.color_f00, null)
        val rect1 = RectF(0f, 0f, 100f, 100f + top)
        canvas.drawRoundRect(rect1, 20f, 20f, mPaint)

        val rect2 = RectF(120f, 0f, 220f, 100f + top)
        canvas.drawRoundRect(rect2, 20f, 20f, mPaint)

        val rect3 = RectF(240f, 0f, 340f, 100f + top)
        canvas.drawRoundRect(rect3, 20f, 20f, mPaint)

        val rect4 = RectF(360f, 0f, 460f, 100f + top)
        canvas.drawRoundRect(rect4, 20f, 20f, mPaint)
    }


}