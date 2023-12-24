package com.margarin.commonweather.domain.models

data class CurrentWeatherModel(
    val name: String,
    val condition: String?,
    val icon_url: Int?,
    val last_updated: String?,
    val wind_kph: String?,
    val wind_dir: String?,
    val wind_dir_img: Int?,
    val temp_c: String?,
    val pressure_mb: String?,
    val humidity: String?,
    val uv: Int?,
    val feels_like: String?,
    val latitude: Float?,
    val longitude: Float?
)
