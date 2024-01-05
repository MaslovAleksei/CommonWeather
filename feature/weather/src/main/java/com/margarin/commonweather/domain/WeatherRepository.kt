package com.margarin.commonweather.domain

import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel

interface WeatherRepository {

    suspend fun loadData(query: String, lang: String)

    suspend fun getCurrentWeather(name: String): CurrentWeatherModel?

    suspend fun getByDaysWeather(name: String): List<ByDaysWeatherModel>?

    suspend fun getByHoursWeather(name: String): List<ByHoursWeatherModel>?

}