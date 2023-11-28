package com.margarin.commonweather.data.remote.apimodels.forecast

data class ForecastDay(
        val date: String,
    val dateEpoch: Long,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>
)
