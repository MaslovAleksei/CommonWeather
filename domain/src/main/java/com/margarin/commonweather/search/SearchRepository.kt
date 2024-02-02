package com.margarin.commonweather.search

import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun search(query: String): List<City>

    val favouriteCities: Flow<List<City>>

    fun observeIsFavourite(cityId: Int): Flow<Boolean>

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(cityId: Int)
}