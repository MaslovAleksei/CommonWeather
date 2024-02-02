package com.margarin.commonweather.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int,
    val name: String?,
    val region: String?,
    val country: String?,
    val lat: Float?,
    val lon: Float?,
    val url: String?,
): Parcelable
