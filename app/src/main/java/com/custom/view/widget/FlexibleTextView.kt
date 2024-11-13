package com.custom.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.custom.view.R

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
class FlexibleTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    val TAG = FlexibleTextView::class.java.simpleName
    var measureWidth = 0
    var measureHeight = 0
    var splitText = ""
    var contentText = ""

    init {
        contentText = text.toString()

    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        if (maxLines != 2) {
//            super.onDraw(canvas)
//            return
//        }
//        Log.e(TAG, "onDraw text:${text} ")
//        Log.e(TAG, "onDraw maxLines:${maxLines} ")
//        Log.e(TAG, "onDraw maxLines:${ellipsize} ")
//        val bound = Rect()
//        //获取文本的宽高
//        paint.getTextBounds(text.toString(), 0, text.length, bound)
//        Log.e(TAG, "onDraw: bound.width() ${bound.width()}  bound.height() ${bound.height()}")
//        var lineWidth = 0f
//        var lineCount = 0
//        for (i in 0 until text.length) {
//            if (lineWidth > width) {
//                break
//            }
//            lineCount = i
//            lineWidth += paint.measureText(text[i].toString())
//        }
//        val textLineOne = text.substring(0, lineCount)
//        val startLine = lineCount
//        lineWidth = 0f
//        for (i in lineCount until text.length) {
//            if (lineWidth > width) {
//                break
//            }
//            lineCount = i
//            lineWidth += paint.measureText(text[i].toString())
//        }
//        var textLineTwo = text.substring(startLine, lineCount - 4)
//        textLineTwo = StringBuilder(textLineTwo).append("...").toString()
////        textLineTwo = StringBuilder(textLineTwo).append("更多").toString()
//        val sp = SpannableString("更多")
//        sp.setSpan(object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                Log.e(TAG, "onClick:111111111111111111111111111111 ", )
//            }
//
//        }, 0, sp.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
//        this.movementMethod=LinkMovementMethod.getInstance()
//        canvas.drawText(textLineOne, 0f, 40f, paint)
//        canvas.drawText(textLineTwo, 0f, 80f, paint)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        Log.e(TAG, "onMeasure text:${getWidth()}   ${getHeight()} ")
        Log.e(TAG, "onMeasure w:${width} ")
        Log.e(TAG, "onMeasure h:${height} ")
        if (maxLines == 2) {
            val bound = Rect()
            //获取文本的宽高
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            Log.e(TAG, "onDraw: bound.width() ${bound.width()}  bound.height() ${bound.height()}")
            var lineWidth = 0f
            var lineCount = 0
            for (i in 0 until text.length) {
                if (lineWidth > measureWidth) {
                    break
                }
                lineCount = i
                lineWidth += paint.measureText(text[i].toString())
            }
            val textLineOne = text.substring(0, lineCount)
            val startLine = lineCount
            lineWidth = 0f
            for (i in lineCount until text.length) {
                if (lineWidth > measureWidth) {
                    break
                }
                lineCount = i
                lineWidth += paint.measureText(text[i].toString())
            }
            var textLineTwo = text.substring(startLine, lineCount - 4)
            textLineTwo = StringBuilder(textLineOne + textLineTwo).append("...").toString()
            val sp = SpannableString(textLineTwo + "更多")
            val conColor = ForegroundColorSpan(resources.getColor(R.color.black, null))

            sp.setSpan(object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds);
                    ds.isUnderlineText = false; // 去掉下划线
                }

                override fun onClick(widget: View) {
                    val sp11 = SpannableString(contentText + "收起")

                    sp11.setSpan(object : ClickableSpan() {
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds);
                            ds.isUnderlineText = false; // 去掉下划线
                        }

                        override fun onClick(widget: View) {
                            Log.e(TAG, "onClick: ")
                            maxLines = 2
                        }
                    }, sp11.length - 2, sp11.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    sp11.setSpan(
                        conColor,
                        sp11.length - 2, sp11.length,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                    text = sp11
                    maxLines = Int.MAX_VALUE
                    movementMethod = LinkMovementMethod.getInstance()
                }

            }, sp.length - 5, sp.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            sp.setSpan(
                conColor,
                sp.length - 5,
                sp.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            splitText = sp.toString()
            text = sp
            this.highlightColor = resources.getColor(android.R.color.transparent, null);
            movementMethod = LinkMovementMethod.getInstance()

        }
    }
}