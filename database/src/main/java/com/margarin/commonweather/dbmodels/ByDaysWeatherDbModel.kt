package com.margarin.commonweather.dbmodels

import androidx.room.Entity

@Entity("weather_by_days", primaryKeys = ["name", "id"])
data class ByDaysWeatherDbModel(
    val name: String,
    val id: Int,
    val date: String?,
    val maxtemp_c: Int?,
    val mintemp_c: Int?,
    val condition: String?,
    val icon_url: String?,
    val maxwind_kph: Int?,
    val chance_of_rain: Int?
)
