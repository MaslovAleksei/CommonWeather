package com.margarin.commonweather.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("weather_current")
data class CurrentWeatherDbModel(
    @PrimaryKey
    val name: String,
    val condition: String?,
    val icon_url: Int?,
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
