package com.margarin.commonweather.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CurrentWeatherDbModel(
    val name: String,
    val condition: String?,
    val icon_url: String?,
    val last_updated: String?,
    val wind_kph: Int?,
    val wind_dir: String?,
    val temp_c: Int?,
    val pressure_mb: Int?,
    val humidity: Int?,
    val uv: Int?,
    val feels_like: Int?,
    val latitude: Float?,
    val longitude: Float?
)
