package com.margarin.commonweather.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.mapper.WeatherMapper
import com.margarin.commonweather.data.remote.ApiService
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
): WeatherRepository {

    override suspend fun loadData(location: String) {
        val currentData = apiService.getCurrentWeather(location)
        val currentDbModel = mapper.mapCurrentDataToCurrentDbModel(currentData)
        currentDao.insertCurrentWeather(currentDbModel)
        Log.d("MyLog", currentDbModel.last_updated)
    }

    override fun loadCurrentWeather(): LiveData<CurrentWeatherModel> {
        return currentDao.loadCurrentWeather().map {
            mapper.mapCurrentDbToEntity(it)
        }
    }

    override fun loadByDaysWeather(): LiveData<List<ByDaysWeatherModel>> {
        TODO("Not yet implemented")
    }

    override fun loadByHoursWeather(): LiveData<List<ByHoursWeatherModel>> {
        TODO("Not yet implemented")
    }


}