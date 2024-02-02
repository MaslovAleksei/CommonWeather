package com.margarin.commonweather

import com.margarin.commonweather.apimodels.forecast.ForecastData
import com.margarin.commonweather.apimodels.search.CityDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query(QUERY_PARAM_CITY) city: String = "",
        @Query(QUERY_PARAM_COUNT_OF_DAYS) days: Int = 3,
    ): ForecastData?

    @GET("search.json")
    suspend fun getCityWeather(
        @Query(QUERY_PARAM_CITY) query: String = "Moscow"
    ): List<CityDto>?

    companion object {
        private const val QUERY_PARAM_CITY = "q"
        private const val QUERY_PARAM_COUNT_OF_DAYS = "days"
    }
}