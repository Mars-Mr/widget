package com.qzn.common.widght

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
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
    val s: String = FlexibleTextView::class.java.simpleName
    private var measureWidth = 0
    private var measureHeight = 0
    private var splitText = ""
    private var contentText = ""
    private var spannableMore: SpannableString? = null
    private var spannablePackUp: SpannableString? = null
    private var conColor: ForegroundColorSpan
    private var listener: OnMoreClickListener? = null
    private var type = -1


    init {
        contentText = text.toString()
        conColor = ForegroundColorSpan(resources.getColor(R.color.white, null))
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = resources.getColor(android.R.color.transparent, null)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        MeasureSpec.getMode(widthMeasureSpec)
        MeasureSpec.getMode(heightMeasureSpec)
        measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        measureHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (maxLines == 2 && lineCount > 2) {
            val bound = Rect()
            //获取文本的宽高
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            var lineWidth = 0f
            var count = 0
            var lineText = ""
            var startIndex = 0
            var showMore = true
            for (i in 0 until maxLines) {
                for (j in startIndex until text.length) {
                    if (lineWidth > measureWidth) {
                        break
                    }
                    count = j
                    lineWidth += paint.measureText(text[j].toString())
                }
                val textLineOne: String = if (i == maxLines - 1) {
                    if (count - startIndex >= 4) {
                        text.substring(startIndex, count - 4)
                    } else if (count - startIndex <= 0) {
                        showMore = false
                        ""
                    } else {
                        showMore = false
                        text.substring(startIndex, count)
                    }
                } else {
                    text.substring(startIndex, count)
                }
                lineText += textLineOne
                lineWidth = 0f
                startIndex = count
            }
            if (spannableMore == null) {
                spannableMore = if (showMore) {
                    lineText = StringBuilder(lineText).append("...").toString()
                    SpannableString(lineText + "更多")
                } else {
                    SpannableString(lineText)
                }
                spannableMore?.setSpan(
                    object : ClickableSpan() {
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = false // 去掉下划线
                            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        }

                        override fun onClick(widget: View) {
                            if (type == 0) {
                                listener?.onClick()
                            } else {
                                if (spannablePackUp == null) {
                                    spannablePackUp = SpannableString(contentText + "收起")
                                    spannablePackUp!!.setSpan(
                                        object : ClickableSpan() {
                                            override fun updateDrawState(ds: TextPaint) {
                                                super.updateDrawState(ds)
                                                ds.isUnderlineText = false // 去掉下划线
                                                ds.typeface =
                                                    Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                                            }

                                            override fun onClick(widget: View) {
                                                maxLines = 2
                                            }
                                        },
                                        spannablePackUp!!.length - 2,
                                        spannablePackUp!!.length,
                                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                                    )
                                    spannablePackUp!!.setSpan(
                                        conColor,
                                        spannablePackUp!!.length - 2, spannablePackUp!!.length,
                                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                                    )
                                }
                                text = spannablePackUp
                                maxLines = Int.MAX_VALUE
                            }
                        }
                    },
                    spannableMore!!.length - 5,
                    spannableMore!!.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                spannableMore!!.setSpan(
                    conColor,
                    spannableMore!!.length - 5,
                    spannableMore!!.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            splitText = spannableMore.toString()
            text = spannableMore

        }
    }

    fun setFlexibleText(t: String) {
        contentText = t
        text = t
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun setOnMoreClickListener(listener: OnMoreClickListener) {
        this.listener = listener
    }

    interface OnMoreClickListener {
        fun onClick()
    }
}