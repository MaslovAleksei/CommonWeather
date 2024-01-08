package com.margarin.commonweather.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.margarin.commonweather.ApiService
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.dao.WeatherDao
import com.margarin.commonweather.data.WeatherMapper
import com.margarin.commonweather.loadFromDataStore
import com.margarin.weather.R
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RefreshWeatherWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val weatherMapper: WeatherMapper,
    private val weatherDao: WeatherDao,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        try {
            val value = loadFromDataStore(context, LOCATION, context.getString(R.string.moscow))
            val forecastData = apiService.getForecastWeather(
                city = value,
                lang = context.getString(R.string.lang)
            )
            if (forecastData != null) {
                weatherDao.addCurrentWeather(
                    weatherMapper.mapForecastDataToCurrentDbModel(forecastData)
                )
                weatherDao.addByDaysWeather(
                    weatherMapper.mapForecastDataToListDayDbModel(forecastData)
                )
                weatherDao.addByHoursWeather(
                    weatherMapper.mapForecastDataToListHoursDbModel(forecastData)
                )

            }
        } catch (_: Exception) {
        }
        return Result.success()

    }

    companion object {
        const val NAME = "RefreshWeatherWorker"

        fun makeRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<RefreshWeatherWorker>(10, TimeUnit.SECONDS)
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    class Factory @Inject constructor(
        private val apiService: ApiService,
        private val weatherMapper: WeatherMapper,
        private val weatherDao: WeatherDao,
    ) : ChildWorkerFactory {

        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {

            return RefreshWeatherWorker(
                context,
                workerParameters,
                apiService,
                weatherMapper,
                weatherDao
            )
        }
    }
}