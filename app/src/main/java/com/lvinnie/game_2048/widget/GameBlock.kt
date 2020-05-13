package com.lvinnie.game_2048.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import com.lvinnie.game_2048.R


/**
 * @author : Vinny
 * date : 2020/5/12
 * desc :
 */
class GameBlock : View {

    var paintBg: Paint
    var paintText: Paint

    var size = 0
    var x: Int
    var y: Int
    var toX: Int
    var toY: Int
    var space: Int
    var cornerSize = 0f
    var rectF: RectF? = null
    var lp: FrameLayout.LayoutParams
    var needMergeAnimation = false

    var number = 0

    constructor(context: Context, size: Int, cornerSize: Float, number: Int, x: Int, y: Int, space: Int): super(context) {

        paintBg = Paint()
        paintBg.isAntiAlias = true
        paintText = Paint()
        paintText.isAntiAlias = true
        paintText.isFakeBoldText = true

        this.size = size
        this.cornerSize = cornerSize
        this.number = number
        this.x = x
        this.y = y
        this.space = space

        toX = -1
        toY = -1

        lp = FrameLayout.LayoutParams(size, size)
        lp.setMargins(size * x + space * (x + 1) , size * y + space * (y + 1), 0, 0)
        layoutParams = lp

    }

    override fun onDraw(canvas: Canvas?) {

        if (rectF == null) {
            rectF = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        }

        var textSize = 0f
        when (number) {
            2 -> {
                paintBg.color = resources.getColor(R.color.bg_number_2)
                paintText.color = resources.getColor(R.color.text_number_2)
                textSize = measuredWidth / 1.5f
                paintText.textSize = textSize
            }
            4 -> {
                paintBg.color = resources.getColor(R.color.bg_number_4)
                paintText.color = resources.getColor(R.color.text_number_4)
                textSize = measuredWidth / 1.5f
                paintText.textSize = textSize
            }
            8 -> {
                paintBg.color = resources.getColor(R.color.bg_number_8)
                paintText.color = resources.getColor(R.color.text_number_8)
                textSize = measuredWidth / 1.5f
                paintText.textSize = textSize
            }
            16 -> {
                paintBg.color = resources.getColor(R.color.bg_number_16)
                paintText.color = resources.getColor(R.color.text_number_16)
                textSize = measuredWidth / 1.5f
                paintText.textSize = textSize
            }
            32 -> {
                paintBg.color = resources.getColor(R.color.bg_number_32)
                paintText.color = resources.getColor(R.color.text_number_32)
                textSize = measuredWidth / 1.5f
                paintText.textSize = textSize
            }
            64 -> {
                paintBg.color = resources.getColor(R.color.bg_number_64)
                paintText.color = resources.getColor(R.color.text_number_64)
                textSize = measuredWidth / 1.5f
                paintText.textSize = textSize
            }
            128 -> {
                paintBg.color = resources.getColor(R.color.bg_number_128)
                paintText.color = resources.getColor(R.color.text_number_128)
                textSize = measuredWidth / 2.1f
                paintText.textSize = textSize
            }
            256 -> {
                paintBg.color = resources.getColor(R.color.bg_number_256)
                paintText.color = resources.getColor(R.color.text_number_256)
                textSize = measuredWidth / 2.1f
                paintText.textSize = textSize
            }
            512 -> {
                paintBg.color = resources.getColor(R.color.bg_number_512)
                paintText.color = resources.getColor(R.color.text_number_512)
                textSize = measuredWidth / 2.1f
                paintText.textSize = textSize
            }
            1024 -> {
                paintBg.color = resources.getColor(R.color.bg_number_1024)
                paintText.color = resources.getColor(R.color.text_number_1024)
                textSize = measuredWidth / 2.5f
                paintText.textSize = textSize
            }
            2048 -> {
                paintBg.color = resources.getColor(R.color.bg_number_2048)
                paintText.color = resources.getColor(R.color.text_number_2048)
                textSize = measuredWidth / 2.5f
                paintText.textSize = textSize
            }
            4096 -> {
                paintBg.color = resources.getColor(R.color.bg_number_4096)
                paintText.color = resources.getColor(R.color.text_number_4096)
                textSize = measuredWidth / 2.5f
                paintText.textSize = textSize
            }
            8192 -> {
                paintBg.color = resources.getColor(R.color.bg_number_4096)
                paintText.color = resources.getColor(R.color.text_number_4096)
                textSize = measuredWidth / 2.5f
                paintText.textSize = textSize
            }
            else -> {
                paintBg.color = resources.getColor(R.color.bg_number_4096)
                paintText.color = resources.getColor(R.color.text_number_4096)
                textSize = measuredWidth / 3.1f
                paintText.textSize = textSize
            }
        }

        canvas?.drawRoundRect(rectF!!, cornerSize, cornerSize, paintBg)

        var fontMetrics = paintText.fontMetricsInt;
        var baseline = (measuredHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top

        canvas?.drawText(number.toString(), (measuredWidth - paintText.measureText(number.toString())) / 2f, baseline.toFloat(), paintText)
    }

    fun setEndX(x: Int) {
        if (x != this.x) {
            toX = x
        }
    }

    fun setEndY(y: Int) {
        if (y != this.y) {
            toY = y
        }
    }

    fun needMoveX(): Boolean {
        if (toX == -1) {
            return false
        }
        return x != toX
    }

    fun needMoveY(): Boolean {
        if (toY == -1) {
            return false
        }
        return y != toY
    }

    fun moveX(percent: Float) {
        lp.leftMargin = (size * x + space * (x + 1) + (size + space) * (toX - x) * percent).toInt()
        layoutParams = lp
    }

    fun moveY(percent: Float) {
        lp.topMargin = (size * y + space * (y + 1) + (size + space) * (toY - y) * percent).toInt()
        layoutParams = lp
    }

    fun endX(container: GameContainer) {
        x = toX
        toX = -1
        if (needMergeAnimation) {
            needMergeAnimation = false
            mergeAnimation()
        }
    }

    fun endY(container: GameContainer) {
        y = toY
        toY = -1
        if (needMergeAnimation) {
            needMergeAnimation = false
            mergeAnimation()
        }
    }

    fun mergeAnimation() {
        number *= 2
        val mergeAnimation1 = ScaleAnimation(1f, 1.25f, 1f, 1.25f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        mergeAnimation1.duration = 50
        mergeAnimation1.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                val mergeAnimation2 = ScaleAnimation(1.25f, 1f, 1.25f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                mergeAnimation2.duration = 40
                startAnimation(mergeAnimation2)
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })
        startAnimation(mergeAnimation1)
    }

    fun merge() {
        needMergeAnimation = true
        bringToFront()
    }
}