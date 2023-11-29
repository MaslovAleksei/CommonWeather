package com.margarin.commonweather.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.mapper.WeatherMapper
import com.margarin.commonweather.data.remote.ApiService
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val application: Application,
    private val apiService: ApiService,
    private val mapper: WeatherMapper,
    private val byDaysDao: ByDaysWeatherDao,
    private val byHoursDao: ByHoursWeatherDao,
    private val currentDao: CurrentWeatherDao
) : WeatherRepository {

    override suspend fun loadData(location: String) {
        try {
            insertCurrentData(location)
            insertByDayData(location)

        } catch (e: Exception) {

        }
    }

    override fun loadCurrentWeather(): LiveData<CurrentWeatherModel> {
        return currentDao.loadCurrentWeather().map {
            mapper.mapCurrentDbToEntity(it)
        }
    }

    override fun loadByDaysWeather(): LiveData<List<ByDaysWeatherModel>> {
        return byDaysDao.loadByDaysWeather().map { list ->
            list.map {
                mapper.mapByDaysDbModelToEntity(it)
            }
        }
    }

    override fun loadByHoursWeather(): LiveData<List<ByHoursWeatherModel>> {
        TODO("Not yet implemented")
    }

    private suspend fun insertCurrentData(location: String) {
        val currentData = apiService.getCurrentWeather(city = location)
        Log.d("MyLog", "currentData $currentData")
        val currentDbModel = mapper.mapCurrentDataToCurrentDbModel(currentData)
        currentDao.insertCurrentWeather(currentDbModel)
    }

    private suspend fun insertByDayData(location: String) {
        val forecastData = apiService.getForecastWeather(city = location)
        Log.d("MyLog", "forecastData $forecastData")
        val byDaysDbModelList = mapper.mapForecastDataToListDayDbModel(forecastData)
        Log.d("MyLog", "byDaysDbModel $byDaysDbModelList")
        byDaysDao.insertByDaysWeather(byDaysDbModelList)
    }


}