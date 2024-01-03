package com.margarin.commonweather.domain

import androidx.lifecycle.LiveData
import com.margarin.commonweather.domain.models.SearchModel

interface SearchRepository {

    suspend fun requestSearchLocation(query: String): List<SearchModel>?

    suspend fun addSearchItem(searchModel: SearchModel)

    fun getSearchList(): LiveData<List<SearchModel>>

    suspend fun deleteSearchItem(searchModel: SearchModel)

}