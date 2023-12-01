package com.margarin.commonweather.domain

import androidx.lifecycle.LiveData
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel

interface WeatherRepository {

    //Weather functions
    suspend fun loadData(query: String)

    fun loadCurrentWeather(): LiveData<CurrentWeatherModel>

    fun loadByDaysWeather(): LiveData<List<ByDaysWeatherModel>>

    fun loadByHoursWeather(): LiveData<List<ByHoursWeatherModel>>

    //Search functions
    suspend fun getSearchLocation(query: String): List<SearchModel>

    suspend fun insertSearchItem(searchModel: SearchModel)

    fun loadSearchList(): LiveData<List<SearchModel>>

    suspend fun deleteSearchItem(searchModel: SearchModel)

    suspend fun getSearchItem(searchId: Int): SearchModel



}