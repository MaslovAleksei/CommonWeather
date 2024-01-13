package com.margarin.commonweather.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.data.WeatherRepositoryImpl
import com.margarin.commonweather.data.workers.factory.ChildWorkerFactory
import com.margarin.commonweather.loadFromDataStore
import com.margarin.weather.R
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RefreshWeatherWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val weatherRepositoryImpl: WeatherRepositoryImpl
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val value = loadFromDataStore(context, LOCATION, context.getString(R.string.moscow))
        weatherRepositoryImpl.loadData(value)
        return Result.success()
    }

    class Factory @Inject constructor(
        private val weatherRepositoryImpl: WeatherRepositoryImpl
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return RefreshWeatherWorker(
                context,
                workerParameters,
                weatherRepositoryImpl
            )
        }
    }

    companion object {
        const val NAME = "RefreshWeatherWorker"

        fun makeRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<RefreshWeatherWorker>(1, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.HOURS)
                .setConstraints(makeConstraints())
                .addTag(NAME)
                .build()
        }

        private fun makeConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}