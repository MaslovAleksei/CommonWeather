package com.margarin.commonweather.domain.models

data class WeatherModel (
    val currentWeatherModel: CurrentWeatherModel?,
    val byDaysWeatherModel:  List<ByDaysWeatherModel>?,
    val byHoursWeatherModel:  List<ByHoursWeatherModel>?
)