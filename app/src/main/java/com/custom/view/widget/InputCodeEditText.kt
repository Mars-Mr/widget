package com.qzn.common.widght

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.InputFilter
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.custom.view.R
import com.custom.view.utils.dp2px
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.math.min

/**
 *
 * @ProjectName: My Application4
 * @Package: com.qzn.myapplication4
 * @ClassName: FlexibleTextView
 * @Description: 描述
 * @Author: 张洪
 * @CreateDate: 2024/11/4 17:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2024/11/4 17:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class InputCodeEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private var measureWidth = 0
    private var measureHeight = 0
    private var boxWidth = dp2px(40f)
    private var boxHeight = dp2px(40f)
    private var intervalOffest = dp2px(10f)
    private var cursorIndexLeft = dp2px(10f).toFloat()
    private var cursorPadding = dp2px(8f).toFloat()
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
        return boxWidth * maxLength + intervalOffest * (maxLength - 1)
    }

    private fun desiredHeight(): Int {
        return boxHeight
    }

    var cursorFlag: Boolean = false
    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawText(canvas)
        if (text!!.length >= maxLength) {
            return
        }
        drawCursor(canvas)
    }

    private fun drawText(canvas: Canvas) {
        mPaint.textSize = dp2px(20f).toFloat()
        mPaint.strokeWidth = dp2px(14f).toFloat()
        var index = 0f
        val bound = Rect()
        mPaint.getTextBounds(text.toString(), 0, text!!.length, bound)

        // 获取FontMetrics对象
        val fontMetrics = mPaint.fontMetrics
        val textHeight = mPaint.descent() - mPaint.ascent()
        val y = boxHeight.toFloat() - (boxHeight.toFloat() - (textHeight / 2 - mPaint.descent()))
        mPaint.color = resources.getColor(R.color.black, null)
        for (i in 0 until text!!.length) {
            if (i == 0) {
                index = boxWidth / 3f
            } else {
                index = i * (boxWidth + intervalOffest) + (boxWidth / 3f)
            }
            canvas.drawText(text!![i].toString(), index, boxHeight.toFloat() * 2 / 3, mPaint)
        }

    }

    private fun drawCursor(canvas: Canvas) {
        for (i in 0 until text!!.length) {
            cursorIndexLeft = (i + 1) * (boxWidth + intervalOffest) + boxWidth / 3f
        }
        if (text!!.isEmpty()) {
            cursorIndexLeft = boxWidth / 3f
        }
        if (cursorFlag) {
            cursorFlag = !cursorFlag
            mPaint.color = resources.getColor(R.color.white, null)
            val rect = RectF(
                cursorIndexLeft,
                cursorPadding / 2,
                cursorIndexLeft + 10f,
                boxHeight.toFloat() - cursorPadding / 2
            )
            canvas.drawRoundRect(rect, 20f, 20f, mPaint)
        } else {
            cursorFlag = !cursorFlag
            mPaint.color = resources.getColor(R.color.black, null)
            val rect = RectF(
                cursorIndexLeft,
                cursorPadding / 2,
                cursorIndexLeft + 10f,
                boxHeight.toFloat() - cursorPadding / 2
            )
            canvas.drawRoundRect(rect, 20f, 20f, mPaint)
        }
    }


    private fun drawBackground(canvas: Canvas) {
        mPaint.color = resources.getColor(R.color.color_ffe6e6e6, null)
        for (i in 0 until maxLength) {
            val rect1 = RectF(
                i * (boxWidth + intervalOffest).toFloat(),
                0f,
                boxWidth + i * (boxWidth + intervalOffest).toFloat(),
                boxHeight.toFloat()
            )
            canvas.drawRoundRect(rect1, 10f, 10f, mPaint)
        }
    }
}