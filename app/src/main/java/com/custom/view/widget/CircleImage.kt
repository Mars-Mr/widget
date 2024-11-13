package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min


class CircleImage(context: Context,attrs: AttributeSet) : AppCompatImageView(context,attrs) {

    lateinit var mBitmap: Bitmap
    var mPaint: Paint

    init {
        mPaint = Paint()
        mPaint.color = context.getColor(R.color.purple_200)
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (drawable is BitmapDrawable) {
            mBitmap = (drawable as BitmapDrawable).bitmap
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (mBitmap != null) {
            //1.绘制圆
//            drawCircle(canvas)
//            drawBorder(canvas)


            // 定义扇形的边界矩形
            val rectF = RectF(0f, 100f, 200f, 200f)
            // 绘制扇形
            // 参数：边界矩形，起始角度，扫过角度，是否包括中心，画笔
            canvas.drawRoundRect(rectF, 700f, 700f, mPaint)
        } else {
            super.onDraw(canvas)
        }
    }


    private fun drawCircle(canvas: Canvas) {
        val scaleX = (width / mBitmap.width.toFloat())
        val scaleY = (height / mBitmap.height.toFloat())
        val matrix = Matrix()
        matrix.setScale(scaleX,scaleY)
        val bitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapShader.setLocalMatrix(matrix)
        mPaint.shader = bitmapShader
        val x = width / 2f
        val y = height / 2f
        val radius = min(x, y)
        canvas.drawCircle(x, y, radius, mPaint)
    }

    private fun drawBorder(canvas: Canvas) {
        mPaint.reset()
        mPaint.isAntiAlias=true
        mPaint.color=Color.RED
        mPaint.style=Paint.Style.STROKE
        mPaint.strokeWidth=20f
        val x = width / 2f
        val y = height / 2f
        val radius = min(x, y)
        canvas.drawCircle(x, y, radius-10f, mPaint)
    }

}
