package com.margarin.commonweather.local.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_table")
data class CityDbModel(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val region: String?,
    val country: String?,
    val lat: Float?,
    val lon: Float?,
    val url: String?
)
