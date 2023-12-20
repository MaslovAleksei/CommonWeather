package com.margarin.commonweather.domain.models

data class ByDaysWeatherModel(
    val name: String,
    val id: Int,
    val date: String?,
    val maxtemp_c: Int?,
    val mintemp_c: Int?,
    val condition: String?,
    val icon_url: Int?,
    val maxwind_kph: Int?,
    val chance_of_rain: Int?,
    val day_of_week: String?
)
