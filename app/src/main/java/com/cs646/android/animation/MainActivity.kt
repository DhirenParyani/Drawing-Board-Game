package com.cs646.android.animation

import android.animation.ObjectAnimator
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener
{

    var lastUpdate: Long = System.currentTimeMillis();
    var lastX: Float = -1f
    var lastY: Float = -1f
    var lastZ: Float = -1f
    var shakeSpeed=600
    private final val TAG:String="MainActivity"

    private lateinit var sensorManager: SensorManager
    var sensor :Sensor?=null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        clearButton.setOnClickListener { _ ->
            drawingBoard.clear()
            drawingBoard.invalidate()
        }
        drawingBoard.setEditON()


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_GAME


        )
        goButton.setOnClickListener { _ ->

            if (goButton.text.equals("Go"))
            {
                clearButton.isEnabled=false
                sensorManager.unregisterListener(this)
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
                sensorManager.registerListener(
                    this,
                    sensor,
                    SensorManager.SENSOR_DELAY_GAME)

                drawingBoard.setEditOff()
                this.animateGoButtonAction()
            }
            else
            {
                clearButton.isEnabled=true
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                sensorManager.unregisterListener(this)
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                sensorManager.registerListener(
                    this,
                    sensor,
                    SensorManager.SENSOR_DELAY_GAME
                )

                drawingBoard.setEditON()
                this.editModeGoButtonAction()


            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
    {

    }

    override fun onSensorChanged(event: SensorEvent?)
    {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER)
        {
            var shakeCount=0;
            var curTime: Long = System.currentTimeMillis()
            var x: Float = event.values[0]
            var y: Float = event.values[1]
            var z: Float = event.values[2]

            //Log.d("Accelerometer", "" + x + "," + y + "," + z)
            if (curTime - lastUpdate > 500)
            {
                val diff: Long = curTime - lastUpdate
                val speed =
                    Math.abs(x + y + z - lastX - lastY - lastZ) / diff * 10000
                lastUpdate = curTime

                lastX = x
                lastY = y
                lastZ = z
                if (speed > shakeSpeed)
                {
                    shakeCount++;
                    Log.d(TAG, "Shake Detected count"+" "+shakeCount)
                    drawingBoard.undoLine()
                }


            }

        }
        if (event?.sensor?.type == Sensor.TYPE_GRAVITY)
        {


            var x: Float = event.values[0]
            var y: Float = event.values[1]
            var z: Float = event.values[2]


            drawingBoard.animateLine(x, y, z)


        }


    }

    override fun onResume()
    {
        super.onResume()
        if (goButton.text.equals("Go"))
        {
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensorManager.unregisterListener(this)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_GAME)
        }
        else{
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensorManager.unregisterListener(this)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_GAME)

        }
    }


    override fun onPause()
    {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    private fun animateGoButtonAction()
    {
        goButton.setText("STOP")
        ObjectAnimator.ofFloat(goButton, "rotation", 0f, 360f).setDuration(1000).start()


    }

    private fun editModeGoButtonAction()
    {
        goButton.setText("Go")
        ObjectAnimator.ofFloat(goButton, "rotation", 360f, 0f).setDuration(1000).start()

    }

}
