package com.example.adsonafonso.circularwaveanimation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

class RippleViews @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val ripplePaint: Paint
    private val rippleGap: Float


    private var maxRadius = 0f
    private var center = PointF(0f, 0f)
    private var initialRadius = 0f

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
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw circles separated by a space the size of rippleGap
        var currentRadius = initialRadius
        while (currentRadius < maxRadius) {
            canvas.drawCircle(center.x, center.y, currentRadius, ripplePaint)
            currentRadius += rippleGap
        }
    }



}