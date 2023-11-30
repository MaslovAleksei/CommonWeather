package com.margarin.commonweather.domain

import androidx.lifecycle.LiveData
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel

interface WeatherRepository {

    suspend fun loadData(query: String)

    fun loadCurrentWeather(): LiveData<CurrentWeatherModel>

    fun loadByDaysWeather(): LiveData<List<ByDaysWeatherModel>>

    fun loadByHoursWeather(): LiveData<List<ByHoursWeatherModel>>

    suspend fun getSearchLocation(location: String): List<SearchModel>

}