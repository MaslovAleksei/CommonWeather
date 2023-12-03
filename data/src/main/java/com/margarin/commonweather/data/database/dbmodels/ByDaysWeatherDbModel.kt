package com.margarin.commonweather.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("weather_by_days")
data class ByDaysWeatherDbModel(
    @PrimaryKey
    val id: Int,
    val date: String?,
    val maxtemp_c: Int?,
    val mintemp_c: Int?,
    val condition: String?,
    val icon_url: String?,
    val maxwind_kph: Int?,
    val chance_of_rain: Int?
)