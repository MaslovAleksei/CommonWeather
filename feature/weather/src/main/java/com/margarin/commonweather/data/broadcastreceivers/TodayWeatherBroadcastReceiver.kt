package com.margarin.commonweather.data.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.margarin.commonweather.data.workers.CurrentWeatherNotificationWorker
import com.margarin.commonweather.log

class TodayWeatherBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {


        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, TodayWeatherBroadcastReceiver::class.java)
        }
    }
}