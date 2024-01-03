package com.margarin.commonweather.data.api.apimodels.forecast

import com.margarin.commonweather.data.api.apimodels.current.Current
import com.margarin.commonweather.data.api.apimodels.current.Location

data class ForecastData(
    val location: Location?,
    val current: Current?,
    val forecast: Forecast?
)
