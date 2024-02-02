package com.margarin.commonweather.weather.models

data class ByDaysWeatherModel(
    val name: String,
    val id: Int,
    val date: String?,
    val maxtemp_c: String?,
    val mintemp_c: String?,
    val condition: String?,
    val icon_url: Int?,
    val maxwind_kph: String?,
    val chance_of_rain: String?,
    val day_of_week: String?
)
