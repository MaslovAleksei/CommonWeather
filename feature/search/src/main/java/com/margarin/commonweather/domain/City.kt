package com.margarin.commonweather.domain

data class City(
    val id: Int,
    val name: String?,
    val region: String?,
    val country: String?,
    val lat: Float?,
    val lon: Float?,
    val url: String?,
)
