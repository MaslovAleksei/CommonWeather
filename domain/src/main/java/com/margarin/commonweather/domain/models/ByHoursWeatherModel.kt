package com.margarin.commonweather.domain.models


data class ByHoursWeatherModel(
    val name: String,
    val id: Int,
    val time: String?,
    val temp_c: Int?,
    val icon_url: String?,
    val wind_kph: Int?,
)
