package com.cs646.android.animation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Line
{
    var startX: Float = 0.toFloat()
    var startY: Float = 0.toFloat()
    var endX: Float = 0.toFloat()
    var endY: Float = 0.toFloat()
    var aStartX:Int=1
    var aStartY:Int=1
    var aEndX:Int=1
    var aEndY:Int=1
    var magnitute:Float=1f




    companion object
    {
        var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG);
    }

    constructor(startX: Float = 0.toFloat(),
        startY: Float = 0.toFloat(),
        endX: Float = 0.toFloat(),
        endY: Float = 0.toFloat())
    {
        this.startX = startX
        this.startY = startY
        this.endX = endX
        this.endY = endY

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.strokeWidth = 10f


    }

    fun drawOn(canvas: Canvas)
    {
        canvas.drawLine(startX, startY, endX, endY, mPaint);
    }


}