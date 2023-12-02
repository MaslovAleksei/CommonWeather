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
            insertCurrentData(currentData)
            insertByDayData(forecastData)
            insertByHourData(forecastData)
        } catch (e: Exception) {
        }
    }

    override fun loadCurrentWeather(): LiveData<com.margarin.commonweather.domain.models.CurrentWeatherModel> {
        return currentDao.loadCurrentWeather().map {
            mapper.mapCurrentDbToEntity(it)
        }
    }

    override fun loadByDaysWeather(): LiveData<List<com.margarin.commonweather.domain.models.ByDaysWeatherModel>> {
        return byDaysDao.loadByDaysWeather().map { list ->
            list.map {
                mapper.mapByDaysDbModelToEntity(it)
            }
        }
    }

    override fun loadByHoursWeather(): LiveData<List<com.margarin.commonweather.domain.models.ByHoursWeatherModel>> {
        return byHoursDao.loadByHoursWeather().map { list ->
            list.map {
                mapper.mapByHoursDbModelToEntity(it)
            }
        }
    }

    override suspend fun getSearchLocation(query: String): List<com.margarin.commonweather.domain.models.SearchModel> {
        var result: List<com.margarin.commonweather.domain.models.SearchModel> = mutableListOf()
        try {
            result = apiService.getSearchWeather(query = query).map {
                mapper.mapSearchDtoToSearchModel(it)
            }
        } catch (e: Exception) {
        }
        return result
    }

    override suspend fun insertSearchItem(searchModel: com.margarin.commonweather.domain.models.SearchModel) {
        val searchDbModel = mapper.mapSearchModelToSearchDbModel(searchModel)
        searchDao.insertSearchItem(searchDbModel)
    }

    override fun loadSearchList(): LiveData<List<com.margarin.commonweather.domain.models.SearchModel>> = searchDao.loadSearchList().map {
        it.map { searchDbModel ->
            mapper.mapSearchDbModelToSearchModel(searchDbModel)
        }
    }

    override suspend fun deleteSearchItem(searchModel: com.margarin.commonweather.domain.models.SearchModel) {
        searchDao.deleteSearchItem(searchModel.id)
    }

    override suspend fun getSearchItem(searchId: Int): com.margarin.commonweather.domain.models.SearchModel {
        val searchModel = searchDao.getSearchItem(searchId)
        return mapper.mapSearchDbModelToSearchModel(searchModel)
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