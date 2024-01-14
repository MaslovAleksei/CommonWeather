package com.margarin.commonweather.data.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.margarin.commonweather.ACTIVITY_REFERENCE
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.data.WeatherRepositoryImpl
import com.margarin.commonweather.data.workers.factory.ChildWorkerFactory
import com.margarin.commonweather.loadFromDataStore
import com.margarin.weather.R
import javax.inject.Inject

class CurrentWeatherNotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val weatherRepositoryImpl: WeatherRepositoryImpl
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        createNotificationChannel(notificationManager)
        notificationManager.notify(NOTIFICATION_ID, createNotification())
        return Result.success()
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createPendingIntent(): PendingIntent {
        val notifyIntent = Intent(context, Class.forName(ACTIVITY_REFERENCE)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private suspend fun createNotification(): Notification {
        val location = loadFromDataStore(context, LOCATION, context.getString(R.string.moscow))
        val weatherModel = weatherRepositoryImpl.getWeather(location)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(createPendingIntent())
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setContentTitle("$location: ${weatherModel.currentWeatherModel?.temp_c}°")
            .setContentText(
                "${context.getString(R.string.feels_like)} " +
                        "${weatherModel.currentWeatherModel?.feels_like}. " +
                        " ${weatherModel.currentWeatherModel?.condition}."
            )
            .setSmallIcon(weatherModel.currentWeatherModel?.icon_url ?: R.drawable.clear_day)
            .build()
    }

    class Factory @Inject constructor(
        private val weatherRepositoryImpl: WeatherRepositoryImpl
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return CurrentWeatherNotificationWorker(
                context,
                workerParameters,
                weatherRepositoryImpl
            )
        }
    }

    companion object {
        const val NAME = "CurrentWeatherNotificationWorker"
        private const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "Current weather notifications"
        private const val NOTIFICATION_ID = 3

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<CurrentWeatherNotificationWorker>()
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}