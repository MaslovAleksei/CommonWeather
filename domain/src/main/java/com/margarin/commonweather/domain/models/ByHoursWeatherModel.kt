package com.margarin.commonweather.domain.models


data class ByHoursWeatherModel(
    val name: String,
    val id: Int,
    val time: String?,
    val temp_c: String?,
    val icon_url: Int?,
    val wind_kph: String?,
)
