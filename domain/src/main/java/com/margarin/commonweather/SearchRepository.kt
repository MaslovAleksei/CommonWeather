package com.margarin.commonweather

import androidx.lifecycle.LiveData
import com.margarin.commonweather.models.SearchModel

interface SearchRepository {

    suspend fun requestSearchLocation(query: String): List<SearchModel>?

    suspend fun addSearchItem(searchModel: SearchModel)

    fun getSearchList(): LiveData<List<SearchModel>>

    suspend fun deleteSearchItem(searchModel: SearchModel)

}