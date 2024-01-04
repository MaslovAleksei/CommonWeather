package com.margarin.commonweather.apimodels.forecast

import com.margarin.commonweather.apimodels.current.Current
import com.margarin.commonweather.apimodels.current.Location

data class ForecastData(
    val location: Location?,
    val current: Current?,
    val forecast: Forecast?
)
