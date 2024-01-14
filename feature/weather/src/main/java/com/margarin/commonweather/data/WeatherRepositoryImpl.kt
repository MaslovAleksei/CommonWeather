package com.margarin.commonweather.data

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import com.margarin.commonweather.ApiFactory.apiService
import com.margarin.commonweather.dao.WeatherDao
import com.margarin.commonweather.data.broadcastreceivers.CurrentWeatherNotificationReceiver
import com.margarin.commonweather.data.workers.RefreshWeatherWorker
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.WeatherModel
import com.margarin.weather.R
import java.util.Calendar
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherMapper: WeatherMapper,
    private val weatherDao: WeatherDao,
    private val application: Application
) : WeatherRepository {

    override suspend fun refreshData(query: String) {
        loadData(query)
        initRefreshWeatherWorker(query)
        initCurrentNotificationManager()
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
