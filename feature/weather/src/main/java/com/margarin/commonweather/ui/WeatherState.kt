package com.margarin.commonweather.ui

import com.margarin.commonweather.domain.models.WeatherModel

sealed class WeatherState

class WeatherInfo(val weather: WeatherModel) : WeatherState()
data object Error : WeatherState()
data object Loading : WeatherState()
data object Success : WeatherState()

