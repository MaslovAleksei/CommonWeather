package com.margarin.commonweather.data.api.apimodels.search

data class Search(
    val id: Int,
    val name: String?,
    val region: String?,
    val country: String?,
    val lat: Float?,
    val lon: Float?,
    val url: String?,
)
