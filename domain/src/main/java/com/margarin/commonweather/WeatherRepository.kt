package com.margarin.commonweather

import com.margarin.commonweather.models.ByDaysWeatherModel
import com.margarin.commonweather.models.ByHoursWeatherModel
import com.margarin.commonweather.models.CurrentWeatherModel

interface WeatherRepository {

    suspend fun loadData(query: String, lang: String)

    suspend fun getCurrentWeather(name: String): CurrentWeatherModel?

    suspend fun getByDaysWeather(name: String): List<ByDaysWeatherModel>?

    suspend fun getByHoursWeather(name: String): List<ByHoursWeatherModel>?

}