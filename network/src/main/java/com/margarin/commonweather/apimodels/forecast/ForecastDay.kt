package com.margarin.commonweather.apimodels.forecast

data class ForecastDay(
    val date: String?,
    val date_epoch: Long?,
    val day: Day?,
    val hour: List<Hour>?
)
