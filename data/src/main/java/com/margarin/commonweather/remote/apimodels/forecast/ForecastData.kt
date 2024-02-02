package com.margarin.commonweather.remote.apimodels.forecast

import com.margarin.commonweather.remote.apimodels.current.Current
import com.margarin.commonweather.remote.apimodels.current.Location

data class ForecastData(
    val location: Location?,
    val current: Current?,
    val forecast: Forecast?
)
