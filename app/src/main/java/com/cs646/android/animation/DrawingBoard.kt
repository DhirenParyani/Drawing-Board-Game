package com.cs646.android.animation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.math.log

import kotlin.math.min
import kotlin.math.sqrt


/**
 * TODO: document your custom view class.
 */
class DrawingBoard : View, View.OnTouchListener
{


    private var startX: Float = 0.toFloat()
    private var startY: Float = 0.toFloat()
    private var endX: Float = 0.toFloat()
    private var endY: Float = 0.toFloat()
    private final val TAG:String="DrawingBoard"
    var maxDistanceForNewLine=10
    var lines = ArrayList<Line>()

    companion object
    {
        var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG);
    }


    constructor(context: Context) : super(context)
    {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context,
        attrs,
        defStyle)
    {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int)
    {
        mPaint?.setStyle(Paint.Style.STROKE);
        mPaint?.setColor(Color.RED);
        mPaint?.strokeWidth = 10f
    }

    fun undoLine()
    {
        if (lines.size > 0)
        {
            lines.removeAt(lines.size - 1)

        }
        else
        {
            resetCanvas()
        }
        invalidate()
    }

    fun saveLines()
    {
        var line = Line()
        line?.startX = startX ?: 0f
        line?.startY = startY ?: 0f
        line?.endX = endX ?: 0f
        line?.endY = endY ?: 0f


        if (line.startX < drawingBoard.left)
            line.startX = drawingBoard.left.toFloat()
        if (line.startX > drawingBoard.right)
            line.startX = drawingBoard.right.toFloat()
        if (line.endX < drawingBoard.left)
            line.endX = drawingBoard.left.toFloat()
        if (line.endX > drawingBoard.right)
            line.endX = drawingBoard.right.toFloat()
        if (line.startY < drawingBoard.top)
            line.startY = drawingBoard.left.toFloat()
        if (line.startY > drawingBoard.bottom)
            line.startY = drawingBoard.bottom.toFloat()
        if (line.endY < drawingBoard.top)
            line.endY = drawingBoard.left.toFloat()
        if (line.endY > drawingBoard.bottom)
            line.endY = drawingBoard.bottom.toFloat()

        lines.add(line)

    }

    fun setEditOff()
    {
        resetCanvas()
        drawingBoard.setOnTouchListener(null)
        invalidate()
    }

    fun setEditON(): Boolean
    {

        setOnTouchListener(this)
        return true
    }


    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)

        for (line in lines)
        {

            line.drawOn(canvas)

        }
        canvas.drawLine(startX, startY, endX, endY, mPaint)
        canvas.save()
    }

    fun resetMagnitude(line: Line)
    {
        line.magnitute = 1f

    }

    fun clear()
    {

        lines.clear()
        resetCanvas()
        invalidate()
    }

    private fun resetCanvas()
    {
        startX = 0.toFloat()
        startY = 0.toFloat()
        endX = 0.toFloat()
        endY = 0.toFloat()

    }


    public fun animateLine(x: Float, y: Float, z: Float)
    {

        for (line in lines)
        {
            if (line.startX - x * (line.aStartX) * line.magnitute <= (drawingBoard.left))
            {

                Log.d(TAG, "Left Edge Detected on Collision with StartX")
                if (x < 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f
                line.aStartX *= -1;


            }

            if (line.endX - x * (line.aEndX) * line.magnitute <= (drawingBoard.left))
            {
                Log.d(TAG, "Left Edge Detected on Collision with endX")

                line.aEndX *= -1
                if (x < 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f


            }
            if (line.startX - x * (line.aStartX) * line.magnitute >= (drawingBoard.right))
            {

                Log.d(TAG, "Right Edge Detected on Collision with startX")
                line.aStartX *= -1
                if (x > 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f


            }
            if (line.endX - x * (line.aEndX) * line.magnitute >= (drawingBoard.right))
            {

                Log.d(TAG, "Right Edge Detected on Collsion with endX")
                line.aEndX *= -1
                if (x > 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f


            }

            if (line.startY + y * (line.aStartY) * line.magnitute <= 0)
            {

                Log.d(TAG, "Top Edge Detected on Collsion with startY")
                if (y > 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f
                line.aStartY *= -1
            }
            if (line.endY + y * (line.aEndY) * line.magnitute <= 0)
            {

                Log.d(TAG, "Top Edge Detected on Collsion with endY")
                if (y > 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f
                line.aEndY *= -1
            }

            if (line.startY + y * (line.aStartY) * line.magnitute >= drawingBoard.bottom)
            {

                Log.d(TAG, "Bottom Edge Detected on Collsion with startY")
                if (y < 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f

                line.aStartY *= -1

            }
            if (line.endY + y * (line.aEndY) * line.magnitute >= drawingBoard.bottom)
            {

                Log.d(TAG, "Bottom Edge Detected on Collsion with endY")
                if (y < 0)
                    resetMagnitude(line)
                else
                    line.magnitute = 0.5f
                line.aEndY *= -1

            }

            line.startX -= x * (line.aStartX) * line.magnitute
            line.startY += y * (line.aStartY) * line.magnitute
            line.endX -= x * (line.aEndX) * line.magnitute
            line.endY += y * (line.aEndY) * line.magnitute

            invalidate()


        }


    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean
    {
        when (event?.action)
        {
            MotionEvent.ACTION_DOWN ->
            {

                startX = event.getX();
                startY = event.getY();
                if (startX < drawingBoard.left)
                    startX = drawingBoard.left.toFloat()
                if (startX > drawingBoard.right)
                    startX = drawingBoard.right.toFloat()
                if (endX < drawingBoard.left)
                    endX = drawingBoard.left.toFloat()
                if (endX > drawingBoard.right)
                    endX = drawingBoard.right.toFloat()

                endX = event.getX();
                endY = event.getY();

                var minDist = Float.MAX_VALUE
                for (line in lines)
                {
                    var endPointDistance =
                        ((line.endX - startX) * (line.endX - startX)) + ((line.endY - startY) * (line.endY - startY))
                    var startPointDistance =
                        ((line.startX - startX) * (line.startX - startX)) + ((line.startY - startY) * (line.startY - startY))
                    endPointDistance = sqrt(endPointDistance)
                    startPointDistance = sqrt(startPointDistance)
                    if (endPointDistance < startPointDistance && endPointDistance < 100 && endPointDistance <= minDist)
                    {
                        startX = line.endX
                        startY = line.endY
                        minDist = min(minDist, endPointDistance)
                    }
                    else if (startPointDistance < maxDistanceForNewLine && startPointDistance <= minDist)
                    {
                        startX = line.startX
                        startY = line.startY
                        minDist = min(minDist, startPointDistance)
                    }
                }

                invalidate()

            }
            MotionEvent.ACTION_MOVE ->
            {
                endX = event.getX();
                endY = event.getY();
                invalidate()
            }
            MotionEvent.ACTION_UP ->
            {
                endX = event.getX();
                endY = event.getY();
                if (endX < drawingBoard.left)
                    endX = drawingBoard.left.toFloat()
                if (endX > drawingBoard.right)
                    endX = drawingBoard.right.toFloat()

                if (endY < drawingBoard.top)
                    endY = drawingBoard.left.toFloat()
                if (endY > drawingBoard.bottom)
                    endY = drawingBoard.bottom.toFloat()
                saveLines()
                resetCanvas()
                invalidate()
            }

        }
        return true
    }


}
