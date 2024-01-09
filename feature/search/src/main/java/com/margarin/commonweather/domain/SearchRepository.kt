package com.margarin.commonweather.domain

import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun requestSearchLocation(query: String): List<SearchModel>?

    suspend fun addSearchItem(searchModel: SearchModel)

    suspend fun  getSavedCityList(): Flow<List<SearchModel>>

    suspend fun deleteSearchItem(searchModel: SearchModel)

}