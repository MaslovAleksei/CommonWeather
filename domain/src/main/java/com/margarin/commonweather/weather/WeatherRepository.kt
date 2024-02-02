package com.margarin.commonweather.weather

import com.margarin.commonweather.weather.models.WeatherModel

interface WeatherRepository {

    suspend fun refreshData(query: String): Boolean

    suspend fun getWeather(name: String): WeatherModel
}