package com.margarin.commonweather.weather.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.margarin.commonweather.weather.workers.CurrentWeatherNotificationWorker

class CurrentWeatherNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniqueWork(
                CurrentWeatherNotificationWorker.NAME,
                ExistingWorkPolicy.REPLACE,
                CurrentWeatherNotificationWorker.makeRequest()
            )
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CurrentWeatherNotificationReceiver::class.java)
        }
    }
}