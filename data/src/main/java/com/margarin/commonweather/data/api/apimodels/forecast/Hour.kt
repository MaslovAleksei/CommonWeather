package com.margarin.commonweather.data.api.apimodels.forecast

import com.margarin.commonweather.data.api.apimodels.current.Condition

data class Hour(
    val time_epoch: Long?,
    val time: String?,
    val temp_c: Float?,
    val temp_f: Float?,
    val is_day: Int?,
    val condition: Condition?,
    val wind_mph: Float?,
    val wind_kph: Float?,
    val wind_degree: Int?,
    val wind_dir: String?,
    val pressure_mb: Float?,
    val pressure_in: Float?,
    val precip_mm: Float?,
    val precip_in: Float?,
    val humidity: Int?,
    val cloud: Int?,
    val feelslike_c: Float?,
    val feelslike_f: Float?,
    val windchill_c: Float?,
    val windchill_f: Float?,
    val heatindex_c: Float?,
    val heatindex_f: Float?,
    val dewpoint_c: Float?,
    val dewpoint_f: Float?,
    val will_it_rain: Int?,
    val chance_of_rain: Int?,
    val will_it_snow: Int?,
    val chance_of_snow: Int?,
    val vis_km: Float?,
    val vis_miles: Float?,
    val gust_mph: Float?,
    val gust_kph: Float?,
    val uv: Float?,


    )