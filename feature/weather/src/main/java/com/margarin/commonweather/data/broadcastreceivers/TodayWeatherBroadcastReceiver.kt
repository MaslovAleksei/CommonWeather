package com.margarin.commonweather.data.broadcastreceivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.margarin.weather.R

class TodayWeatherBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager
            createNotificationChannel(notificationManager)
            val notificationTitle = intent?.getStringExtra("notificationTitle")
            val notificationText = intent?.getStringExtra("notificationText")
            val notification = NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.clear_day)
                .build()
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 3


        fun newIntent(context: Context): Intent {
            return Intent(context, TodayWeatherBroadcastReceiver::class.java)
        }
    }
}