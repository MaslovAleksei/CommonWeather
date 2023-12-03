package com.margarin.commonweather.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("weather_by_hours")
data class ByHoursWeatherDbModel(
    @PrimaryKey
    val id: Int,
    val time: String?,
    val temp_c: Int?,
    val icon_url: String?,
    val wind_kph: Int?,
)
