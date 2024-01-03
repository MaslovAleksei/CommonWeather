package com.margarin.commonweather.data.repository

import androidx.lifecycle.map
import com.margarin.commonweather.data.database.dao.SearchDao
import com.margarin.commonweather.data.database.dao.WeatherDao
import com.margarin.commonweather.data.mapper.WeatherMapper
import com.margarin.commonweather.data.remote.ApiService
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.commonweather.domain.models.WeatherModel
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: WeatherMapper,
    private val weatherDao: WeatherDao,
    private val searchDao: SearchDao
) : WeatherRepository {

    override suspend fun loadData(query: String, lang: String) {
        try {
            val forecastData = apiService.getForecastWeather(city = query, lang = lang)
            if (forecastData != null) {
                weatherDao.addWeather(mapper.mapForecastDataToDbModel(forecastData))
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun getWeatherModel(name: String): WeatherModel? {
        return weatherDao.getWeather(name)?.let { mapper.mapWeatherDbModelToEntity(it) }
    }


    override suspend fun requestSearchLocation(query: String): List<SearchModel>? {
        var result: List<SearchModel>? = mutableListOf()
        try {
            result = apiService.getSearchWeather(query = query)?.map {
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

    override fun getSearchList() = searchDao.getSearchList().map {
        it.map { searchDbModel ->
            mapper.mapSearchDbModelToSearchModel(searchDbModel)
        }
    }

    override suspend fun deleteSearchItem(searchModel: SearchModel) {
        searchDao.deleteSearchItem(searchModel.id)
    }
}