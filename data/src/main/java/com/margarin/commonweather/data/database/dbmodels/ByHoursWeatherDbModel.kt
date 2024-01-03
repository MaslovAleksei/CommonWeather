package com.margarin.commonweather.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey


data class ByHoursWeatherDbModel(
    val time: String,
    val currentTime: String?,
    val temp_c: Int?,
    val icon_url: String?,
    val wind_kph: Int?,
)
