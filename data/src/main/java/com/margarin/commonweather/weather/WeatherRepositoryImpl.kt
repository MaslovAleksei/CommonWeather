package com.margarin.commonweather.weather

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import com.margarin.commonweather.remote.ApiFactory.apiService
import com.margarin.commonweather.weather.broadcastreceivers.CurrentWeatherNotificationReceiver
import com.margarin.commonweather.weather.models.WeatherModel
import com.margarin.commonweather.weather.workers.RefreshWeatherWorker
import java.util.Calendar
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherMapper: WeatherMapper,
    private val weatherDao: com.margarin.commonweather.local.dao.WeatherDao,
    private val application: Application
) : WeatherRepository {

    override suspend fun refreshData(query: String): Boolean {
        loadData(query)
        initRefreshWeatherWorker(query)
        initCurrentNotificationManager()
        return true
    }

    override suspend fun getWeather(name: String) =
        WeatherModel(
            weatherMapper.mapCurrentDbToEntity(weatherDao.getCurrentWeather(name)),
            weatherDao.getByDaysWeather(name)?.map { weatherMapper.mapByDaysDbModelToEntity(it) },
            weatherDao.getByHoursWeather(name)?.map { weatherMapper.mapByHoursDbModelToEntity(it) }
        )

    internal suspend fun loadData(query: String) {
        try {
            val forecastData = apiService.getForecastWeather(city = query)
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

    private fun initRefreshWeatherWorker(location: String) {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniquePeriodicWork(
            RefreshWeatherWorker.NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            RefreshWeatherWorker.makeRequest(location)
        )
    }

    private fun initCurrentNotificationManager() {
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 20)
        }
        val alarmIntent = CurrentWeatherNotificationReceiver.newIntent(application)
        val pendingIntent = PendingIntent.getBroadcast(
            application,
            100,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HALF_DAY,
            pendingIntent
        )
    }
}
