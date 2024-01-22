package com.margarin.commonweather.presentation

import com.margarin.commonweather.domain.models.WeatherModel

sealed class WeatherState {

    class Info(val weather: WeatherModel) : WeatherState()
    data object Error : WeatherState()
    data object Loading : WeatherState()
    data object Success : WeatherState()
    data object Initial : WeatherState()
}

