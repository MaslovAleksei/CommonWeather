package com.margarin.commonweather.domain

import com.margarin.commonweather.domain.models.WeatherModel

interface WeatherRepository {

    suspend fun loadData(query: String, lang: String)

    suspend fun getWeather(name: String): WeatherModel?
}