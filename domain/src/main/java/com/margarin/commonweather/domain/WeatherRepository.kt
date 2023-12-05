package com.margarin.commonweather.domain

import androidx.lifecycle.LiveData
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel

interface WeatherRepository {

    //Weather functions
    suspend fun loadData(query: String)

    fun getCurrentWeather(): LiveData<CurrentWeatherModel>

    fun getByDaysWeather(): LiveData<List<ByDaysWeatherModel>>

    fun getByHoursWeather(): LiveData<List<ByHoursWeatherModel>>

    //Search functions
    suspend fun getSearchLocation(query: String): List<SearchModel>

    suspend fun addSearchItem(searchModel: SearchModel)

    fun getSearchList(): LiveData<List<SearchModel>>

    suspend fun deleteSearchItem(searchModel: SearchModel)

    suspend fun getSearchItem(searchId: Int): SearchModel



}