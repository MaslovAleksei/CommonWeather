package com.margarin.commonweather.data.database.dbmodels


data class ByDaysWeatherDbModel(
    val date: String,
    val maxtemp_c: Int?,
    val mintemp_c: Int?,
    val condition: String?,
    val icon_url: String?,
    val maxwind_kph: Int?,
    val chance_of_rain: Int?
)
