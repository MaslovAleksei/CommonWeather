package com.margarin.commonweather.data.database.dbmodels

import androidx.room.Embedded
import androidx.room.Entity

@Entity("weather_table", primaryKeys = ["name"])
data class WeatherDbModel(
    val name: String,
    @Embedded
    val currentWeatherDbModel: CurrentWeatherDbModel?,
    val byDaysWeatherDbModel: List<ByDaysWeatherDbModel>?,
    val byHoursWeatherDbModel: List<ByHoursWeatherDbModel>?
)
