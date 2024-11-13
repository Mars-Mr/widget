package com.custom.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.custom.view.R
import kotlin.math.min


class CustomerView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    final val TAG = CustomerView::class.java.name
    lateinit var mBitmap: Bitmap
    var mPaint: Paint

    init {
        Log.e(TAG, ":init ")
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = context.getColor(R.color.white)
        mPaint.style = Paint.Style.FILL
    }

    /**
     * 每次页面刷新就会调用
     */
    override fun onDraw(canvas: Canvas) {
        if (mBitmap != null) {
            drawCircle(canvas)
        } else {
            super.onDraw(canvas)
        }
        Log.e(TAG, "onDraw   drawable : ")
    }

    private fun drawCircle(canvas: Canvas) {
        val mBitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mPaint.shader = mBitmapShader
        //1.画布画圆
        canvas.drawCircle(
            (width / 2f) ,
            (height / 2f) ,
            min(width/2f, height/2f),
            mPaint
        )

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG, ": onMeasure")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.e(TAG, ": onLayout")
        val current = drawable
        if (current is BitmapDrawable) {
            mBitmap = (drawable as BitmapDrawable).bitmap
        }
    }

}