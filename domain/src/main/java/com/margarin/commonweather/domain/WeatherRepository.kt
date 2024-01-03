package com.margarin.commonweather.domain

import androidx.lifecycle.LiveData
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.commonweather.domain.models.WeatherModel

interface WeatherRepository {

    //Weather functions
    suspend fun loadData(query: String, lang: String)

    suspend fun getWeatherModel(name: String): WeatherModel?

    //Search functions
    suspend fun requestSearchLocation(query: String): List<SearchModel>?

    suspend fun addSearchItem(searchModel: SearchModel)

    fun getSearchList(): LiveData<List<SearchModel>>

    suspend fun deleteSearchItem(searchModel: SearchModel)

}