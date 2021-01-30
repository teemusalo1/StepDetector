package com.example.stepdetector

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity(), SensorEventListener {
    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null
    private var isSensorPresent = false
    private var steps: TextView? = null
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "123"
    private val description = "Test Notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        steps = findViewById<TextView>(R.id.texti)
        steps!!.text = "Walk to get message"
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
                != null)
        {
            mSensor =
                    mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            isSensorPresent = true
            Log.d("check", "sensor exists")

        }
        else
        {
            isSensorPresent = false

        }

        super.onResume()
        if(isSensorPresent)
        {
            mSensorManager?.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

    }



    override fun onSensorChanged(event: SensorEvent?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        if (event != null) {

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager .IMPORTANCE_HIGH)
                notificationChannel.lightColor = Color.BLUE
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
                builder = Notification.Builder(this, channelId).setContentTitle(getString(R.string.nice)).setContentText(getString(R.string.detection)).setSmallIcon(R.drawable.ic_launcher_background).setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable
                        .ic_launcher_background)).setContentIntent(pendingIntent)
            }
            notificationManager.notify(12345, builder.build())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}
