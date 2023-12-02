package com.margarin.commonweather.data.remote.apimodels.forecast

import com.margarin.commonweather.data.remote.apimodels.current.Current
import com.margarin.commonweather.data.remote.apimodels.current.Location

data class ForecastData(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)
