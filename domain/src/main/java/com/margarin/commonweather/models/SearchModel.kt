package com.margarin.commonweather.models

data class SearchModel(
    val id: Int,
    val name: String?,
    val region: String?,
    val country: String?,
    val lat: Float?,
    val lon: Float?,
    val url: String?,
)