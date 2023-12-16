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
            val forecastData = apiService.getForecastWeather(city = query)
            addCurrentData(forecastData)
            addByDayData(forecastData)
            addByHourData(forecastData)
        } catch (_: Exception) {
        }
    }

    override suspend fun getCurrentWeather(name: String) =
        mapper.mapCurrentDbToEntity(currentDao.getCurrentWeather(name))

    override suspend fun getByDaysWeather(name: String) =
        byDaysDao.getByDaysWeather(name).map { mapper.mapByDaysDbModelToEntity(it) }


    override suspend fun getByHoursWeather(name: String) =
        byHoursDao.getByHoursWeather(name).map { mapper.mapByHoursDbModelToEntity(it) }

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

    private suspend fun addCurrentData(forecastData: ForecastData) {
        val currentDbModel = mapper.mapForecastDataToCurrentDbModel(forecastData)
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