package com.margarin.commonweather.data

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import com.margarin.commonweather.ApiService
import com.margarin.commonweather.dao.WeatherDao
import com.margarin.commonweather.data.workers.RefreshWeatherWorker
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.WeatherModel
import com.margarin.weather.R
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val weatherMapper: WeatherMapper,
    private val weatherDao: WeatherDao,
    private val application: Application
) : WeatherRepository {

    override suspend fun refreshData(query: String) {
        loadData(query)
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniquePeriodicWork(
            RefreshWeatherWorker.NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            RefreshWeatherWorker.makeRequest()
        )
    }

    override suspend fun getWeather(name: String) = WeatherModel(
        weatherMapper.mapCurrentDbToEntity(weatherDao.getCurrentWeather(name)),
        weatherDao.getByDaysWeather(name)?.map { weatherMapper.mapByDaysDbModelToEntity(it) },
        weatherDao.getByHoursWeather(name)?.map { weatherMapper.mapByHoursDbModelToEntity(it) }
    )

    suspend fun loadData(query: String) {
        val lang = application.getString(R.string.lang)
        try {
            val forecastData = apiService.getForecastWeather(city = query, lang = lang)
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
            ListenableWorker.Result.failure()
        }
    }
}