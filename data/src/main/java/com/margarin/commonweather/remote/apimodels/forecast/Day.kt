package com.margarin.commonweather.remote.apimodels.forecast

import com.margarin.commonweather.remote.apimodels.current.Condition

data class Day(
    val maxtemp_c: Float?,
    val maxtemp_f: Float?,
    val mintemp_c: Float?,
    val mintemp_f: Float?,
    val avgtemp_c: Float?,
    val avgtemp_f: Float?,
    val maxwind_mph: Float?,
    val maxwind_kph: Float?,
    val totalprecip_mm: Float?,
    val totalprecip_in: Float?,
    val totalsnow_cm: Float?,
    val avgvis_km: Float?,
    val avgvis_miles: Float?,
    val avghumidity: Float?,
    val daily_will_it_rain: Int?,
    val daily_chance_of_rain: Int?,
    val daily_will_it_snow: Int?,
    val daily_chance_of_snow: Int?,
    val condition: Condition?,
    val uv: Float?,
    )
