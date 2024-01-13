package com.margarin.commonweather.data.workers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.data.WeatherRepositoryImpl
import com.margarin.commonweather.data.workers.factory.ChildWorkerFactory
import com.margarin.commonweather.loadFromDataStore
import com.margarin.weather.R
import javax.inject.Inject

class CurrentWeatherNotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val weatherRepositoryImpl: WeatherRepositoryImpl,
) : CoroutineWorker(context, workerParameters) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override suspend fun doWork(): Result {
        val location = loadFromDataStore(context, LOCATION, context.getString(R.string.moscow))
        val weatherModel = weatherRepositoryImpl.getWeather(location)
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("$location: ${weatherModel.currentWeatherModel?.temp_c}°")
            .setContentText(
                "${context.getString(R.string.feels_like)} " +
                        "${weatherModel.currentWeatherModel?.feels_like}. " +
                        " ${weatherModel.currentWeatherModel?.condition}."
            )
            .setSmallIcon(weatherModel.currentWeatherModel?.icon_url ?: R.drawable.clear_day)
            .build()

        createNotificationChannel(notificationManager)
        notificationManager.notify(NOTIFICATION_ID, notification)

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