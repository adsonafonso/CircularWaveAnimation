package com.example.adsonafonso.circularwaveanimation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class RippleViews @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ripplePaint: Paint
    private val rippleGap: Float

    private var maxRadius = 0f
    private var center = PointF(0f, 0f)
    private var initialRadius = 0f

    private var rippleAnimator: ValueAnimator? = null
    private var rippleRadiusOffset = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    private val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // Highlight only the areas already touched on the canvas
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
    // gradient colors
    private val green = Color.GREEN
    // solid green in the center, transparent green at the edges
    private val gradientColors =
        intArrayOf(green, modifyAlpha(green, 0.80f),
            modifyAlpha(green, 0.05f))
    private val gradientMatrix = Matrix()



    init {
        val attrs = context.obtainStyledAttributes(attrs,R.styleable.RipplesView, defStyleAttr,0)

        ripplePaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = attrs.getColor(R.styleable.RipplesView_rippleColor, 0)
            strokeWidth = attrs.getDimension(R.styleable.RipplesView_rippleStrokeWidth,0f)
            style = Paint.Style.STROKE
        }

        rippleGap = attrs.getDimension(R.styleable.RipplesView_rippleGap, 50f)
        attrs.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //set the center of all circles to be center of the view
        center.set(w / 2f, h / 2f)
        maxRadius = Math.hypot(center.x.toDouble(), center.y.toDouble()).toFloat()
        initialRadius = w / rippleGap
        //Create gradient after getting sizing information
        gradientPaint.shader = RadialGradient(
            center.x, center.y, w / 2f,
            gradientColors, null, Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw circles separated by a space the size of rippleGap
        var currentRadius = initialRadius + rippleRadiusOffset
        while (currentRadius < maxRadius) {
            canvas.drawCircle(center.x, center.y, currentRadius, ripplePaint)
            currentRadius += rippleGap
        }

        canvas.drawPaint(gradientPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rippleAnimator = ValueAnimator.ofFloat(0f, rippleGap).apply {
            addUpdateListener {
                rippleRadiusOffset = it.animatedValue as Float
            }
            duration = 1500L
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    override fun onDetachedFromWindow() {
        rippleAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    private fun modifyAlpha(color: Int, alpha: Float): Int {
        return color and 0x00ffffff or ((alpha * 255).toInt() shl 24)
    }


}