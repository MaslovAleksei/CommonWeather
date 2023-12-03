package com.margarin.commonweather.data.remote.apimodels.current

data class Location(
    val name: String?,
    val region: String?,
    val country: String?,
    val lat: Float?,
    val lon: Float?,
    val tz_id: String?,
    val localtime_epoch: Long?,
    val localtime: String?
)
