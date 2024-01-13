package com.margarin.commonweather.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.data.WeatherRepositoryImpl
import com.margarin.commonweather.data.workers.factory.ChildWorkerFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RefreshWeatherWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val weatherRepositoryImpl: WeatherRepositoryImpl
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val location = inputData.getString(LOCATION)
        weatherRepositoryImpl.loadData(location.toString())
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

        fun makeRequest(location: String): PeriodicWorkRequest {
            val data = Data.Builder()
                .putString(LOCATION, location)
                .build()

            return PeriodicWorkRequestBuilder<RefreshWeatherWorker>(1, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(makeConstraints())
                .addTag(NAME)
                .build()
        }

        private fun makeConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}