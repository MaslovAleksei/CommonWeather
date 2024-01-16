package com.margarin.commonweather.domain

import com.margarin.commonweather.domain.models.WeatherModel

interface WeatherRepository {

    suspend fun refreshData(query: String): Boolean

    suspend fun getWeather(name: String): WeatherModel
}