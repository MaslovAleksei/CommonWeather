package com.margarin.commonweather.domain

import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun search(query: String): List<City>?

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(city: City)

    suspend fun  getSavedCityList(): Flow<List<City>>

}