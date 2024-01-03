package com.margarin.commonweather.data.repository

import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.mapper.WeatherMapper
import com.margarin.commonweather.data.api.ApiService
import com.margarin.commonweather.domain.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val weatherMapper: WeatherMapper,
    private val byDaysDao: ByDaysWeatherDao,
    private val byHoursDao: ByHoursWeatherDao,
    private val currentDao: CurrentWeatherDao,
) : WeatherRepository {

    override suspend fun loadData(query: String, lang: String) {
        try {
            val forecastData = apiService.getForecastWeather(city = query, lang = lang)
            if (forecastData != null) {
                currentDao.addCurrentWeather(weatherMapper.mapForecastDataToCurrentDbModel(forecastData))
                byDaysDao.addByDaysWeather(weatherMapper.mapForecastDataToListDayDbModel(forecastData))
                byHoursDao.addByHoursWeather(weatherMapper.mapForecastDataToListHoursDbModel(forecastData))
            }
        } catch (_: Exception) { }
    }

    override suspend fun getCurrentWeather(name: String) =
        weatherMapper.mapCurrentDbToEntity(currentDao.getCurrentWeather(name))

    override suspend fun getByDaysWeather(name: String) =
        byDaysDao.getByDaysWeather(name)?.map { weatherMapper.mapByDaysDbModelToEntity(it) }

    override suspend fun getByHoursWeather(name: String) =
        byHoursDao.getByHoursWeather(name)?.map { weatherMapper.mapByHoursDbModelToEntity(it) }
}