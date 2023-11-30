package com.margarin.commonweather.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.mapper.WeatherMapper
import com.margarin.commonweather.data.remote.ApiService
import com.margarin.commonweather.data.remote.apimodels.current.CurrentData
import com.margarin.commonweather.data.remote.apimodels.forecast.ForecastData
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
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
            val currentData = apiService.getCurrentWeather(city = location)
            val forecastData = apiService.getForecastWeather(city = location)
            insertCurrentData(currentData)
            insertByDayData(forecastData)
            insertByHourData(forecastData)
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
        return byHoursDao.loadByHoursWeather().map { list ->
            list.map {
                mapper.mapByHoursDbModelToEntity(it)
            }
        }
    }

    private suspend fun insertCurrentData(currentData: CurrentData) {
        val currentDbModel = mapper.mapCurrentDataToCurrentDbModel(currentData)
        currentDao.insertCurrentWeather(currentDbModel)
    }

    private suspend fun insertByDayData(forecastData: ForecastData) {
        val byDaysDbModelList = mapper.mapForecastDataToListDayDbModel(forecastData)
        byDaysDao.insertByDaysWeather(byDaysDbModelList)
    }

    private suspend fun insertByHourData(forecastData: ForecastData) {
        val byHoursDbModelList = mapper.mapForecastDataToListHoursDbModel(forecastData)
        byHoursDao.insertByHoursWeather(byHoursDbModelList)
    }


}