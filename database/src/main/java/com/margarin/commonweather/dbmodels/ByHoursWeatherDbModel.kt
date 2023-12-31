package com.margarin.commonweather.dbmodels

import androidx.room.Entity

@Entity("weather_by_hours", primaryKeys = ["name", "id"])
data class ByHoursWeatherDbModel(
    val name: String,
    val id: Int,
    val time: String?,
    val currentTime: String?,
    val temp_c: Int?,
    val icon_url: String?,
    val wind_kph: Int?,
)
