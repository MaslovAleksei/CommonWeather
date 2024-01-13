package com.margarin.commonweather.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.data.WeatherRepositoryImpl
import com.margarin.commonweather.data.workers.factory.ChildWorkerFactory
import com.margarin.commonweather.loadFromDataStore
import com.margarin.commonweather.log
import com.margarin.weather.R
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrentWeatherNotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val weatherRepositoryImpl: WeatherRepositoryImpl
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        log("doWork")
        val location = loadFromDataStore(context, LOCATION, context.getString(R.string.moscow))

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        createNotificationChannel(notificationManager)
        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setContentTitle(createNotificationTitle(location))
            .setContentText("notificationText")
            .setSmallIcon(R.drawable.clear_day)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)

        return Result.success()

    }

    private suspend fun createNotificationTitle(location: String): String {
        log("createNotificationTitle")
        val weatherModel = weatherRepositoryImpl.getWeather(location)
        return "$location: ${weatherModel.currentWeatherModel?.temp_c}°"
    }


    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
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

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
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