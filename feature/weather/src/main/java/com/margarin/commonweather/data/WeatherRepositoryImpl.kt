package com.margarin.commonweather.data

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import com.margarin.commonweather.ApiService
import com.margarin.commonweather.dao.WeatherDao
import com.margarin.commonweather.data.broadcastreceivers.TodayWeatherBroadcastReceiver
import com.margarin.commonweather.data.workers.RefreshWeatherWorker
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.WeatherModel
import com.margarin.weather.R
import java.util.Calendar
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val weatherMapper: WeatherMapper,
    private val weatherDao: WeatherDao,
    private val application: Application
) : WeatherRepository {

    override suspend fun refreshData(query: String) {
        loadData(query)
        initRefreshWeatherWorker()
        initTodayNotification()
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

    private fun initRefreshWeatherWorker() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniquePeriodicWork(
            RefreshWeatherWorker.NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            RefreshWeatherWorker.makeRequest()
        )
    }

    private fun initTodayNotification() {
        val alarmManager = application.getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()//.apply {
            calendar.add(Calendar.SECOND, 2)
        val alarmIntent = TodayWeatherBroadcastReceiver.newIntent(application)
        val pendingIntent = PendingIntent.getBroadcast(application, 100, alarmIntent,
            PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )



        //            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 7)
//            set(Calendar.MINUTE, 20)
        //       }
//        val alarmIntent = TodayWeatherBroadcastReceiver.newIntent(application).apply {
//            putExtra("notificationTitle", "notificationTitle")
//            putExtra("notificationText", "notificationText")
//        }.let { intent ->
//            PendingIntent.getBroadcast(
//                application,
//                100,
//                intent,
//                PendingIntent.FLAG_IMMUTABLE
//            )
//        }
//        alarmManager.setInexactRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY,
//            alarmIntent
//        )
    }
}