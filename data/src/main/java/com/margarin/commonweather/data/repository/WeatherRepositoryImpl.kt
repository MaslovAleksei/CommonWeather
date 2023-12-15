package com.margarin.commonweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.database.dao.SearchDao
import com.margarin.commonweather.data.mapper.WeatherMapper

import com.margarin.commonweather.data.remote.ApiService
import com.margarin.commonweather.data.remote.apimodels.current.CurrentData
import com.margarin.commonweather.data.remote.apimodels.forecast.ForecastData
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: WeatherMapper,
    private val byDaysDao: ByDaysWeatherDao,
    private val byHoursDao: ByHoursWeatherDao,
    private val currentDao: CurrentWeatherDao,
    private val searchDao: SearchDao
) : com.margarin.commonweather.domain.WeatherRepository {

    override suspend fun loadData(query: String) {
        try {
            val currentData = apiService.getCurrentWeather(city = query)
            val forecastData = apiService.getForecastWeather(city = query)
            addCurrentData(currentData)
            addByDayData(forecastData)
            addByHourData(forecastData)
        } catch (_: Exception) {
        }
    }

    override suspend fun getCurrentWeather(name: String) : CurrentWeatherModel {
        val currentWeatherModel = currentDao.getCurrentWeather(name = name)
        return mapper.mapCurrentDbToEntity(currentWeatherModel)
    }


    override fun getByDaysWeather(): LiveData<List<ByDaysWeatherModel>> {
        return byDaysDao.getByDaysWeather().map { list ->
            list.map {
                mapper.mapByDaysDbModelToEntity(it)
            }
        }
    }

    override fun getByHoursWeather(): LiveData<List<ByHoursWeatherModel>> {
        return byHoursDao.getByHoursWeather().map { list ->
            list.map {
                mapper.mapByHoursDbModelToEntity(it)
            }
        }
    }

    override suspend fun getSearchLocation(query: String): List<SearchModel> {
        var result: List<SearchModel> = mutableListOf()
        try {
            result = apiService.getSearchWeather(query = query).map {
                mapper.mapSearchDtoToSearchModel(it)
            }
        } catch (_: Exception) {
        }
        return result
    }

    override suspend fun addSearchItem(searchModel: SearchModel) {
        val searchDbModel = mapper.mapSearchModelToSearchDbModel(searchModel)
        searchDao.addSearchItem(searchDbModel)
    }

    override fun getSearchList(): LiveData<List<SearchModel>> = searchDao.getSearchList().map {
        it.map { searchDbModel ->
            mapper.mapSearchDbModelToSearchModel(searchDbModel)
        }
    }

    override suspend fun deleteSearchItem(searchModel: SearchModel) {
        searchDao.deleteSearchItem(searchModel.id)
    }

    override suspend fun getSearchItem(searchId: Int): SearchModel {
        val searchModel = searchDao.getSearchItem(searchId)
        return mapper.mapSearchDbModelToSearchModel(searchModel)
    }

    private suspend fun addCurrentData(currentData: CurrentData) {
        val currentDbModel = mapper.mapCurrentDataToCurrentDbModel(currentData)
        currentDao.addCurrentWeather(currentDbModel)
    }

    private suspend fun addByDayData(forecastData: ForecastData) {
        val byDaysDbModelList = mapper.mapForecastDataToListDayDbModel(forecastData)
        byDaysDao.addByDaysWeather(byDaysDbModelList)
    }

    private suspend fun addByHourData(forecastData: ForecastData) {
        val byHoursDbModelList = mapper.mapForecastDataToListHoursDbModel(forecastData)
        byHoursDao.addByHoursWeather(byHoursDbModelList)
    }




}