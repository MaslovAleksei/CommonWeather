package com.margarin.commonweather.weather.models

data class WeatherModel (
    val currentWeatherModel: CurrentWeatherModel?,
    val byDaysWeatherModel:  List<ByDaysWeatherModel>?,
    val byHoursWeatherModel:  List<ByHoursWeatherModel>?
)