package com.margarin.commonweather

import com.margarin.commonweather.apimodels.forecast.ForecastData
import com.margarin.commonweather.apimodels.search.Search
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_CITY) city: String = "",
        @Query(QUERY_PARAM_QUANTITY_OF_DAYS) days: Int = 5,
        @Query(QUERY_PARAM_AQI) aqi: String = "no",
        @Query(QUERY_PARAM_ALERTS) alerts: String = "no",
        @Query(QUERY_PARAM_LANG) lang: String = "en"
    ): ForecastData?

    @GET("search.json")
    suspend fun getSearchWeather(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_CITY) query: String = "Moscow"
    ): List<Search>?


    companion object {
        private const val API_KEY = "98e02ea3daac496daac80826231612"
        private const val QUERY_PARAM_API_KEY = "key"
        private const val QUERY_PARAM_CITY = "q"
        private const val QUERY_PARAM_AQI = "aqi"
        private const val QUERY_PARAM_LANG = "lang"
        private const val QUERY_PARAM_ALERTS = "alerts"
        private const val QUERY_PARAM_QUANTITY_OF_DAYS = "days"
    }
}