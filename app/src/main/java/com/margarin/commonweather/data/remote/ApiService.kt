package com.margarin.commonweather.data.remote

import com.margarin.commonweather.data.remote.apimodels.current.CurrentData
import com.margarin.commonweather.data.remote.apimodels.forecast.ForecastData
import com.margarin.commonweather.data.remote.apimodels.search.Search
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_CITY) city: String = "",
        @Query(QUERY_PARAM_AQI) aqi: String = "no",
        @Query(QUERY_PARAM_LANG) lang: String = "ru"
    ): CurrentData

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_CITY) city: String = "",
        @Query(QUERY_PARAM_QUANTITY_OF_DAYS) days: Int = 5,
        @Query(QUERY_PARAM_AQI) aqi: String = "no",
        @Query(QUERY_PARAM_ALERTS) alerts: String = "no",
        @Query(QUERY_PARAM_LANG) lang: String = "ru"
    ): ForecastData

    @GET("search.json")
    suspend fun getSearchWeather(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_CITY) query: String = "Moscow"
    ): List<Search>


    companion object {
        private const val API_KEY = "39b223a021994097b0573715232910"
        private const val QUERY_PARAM_API_KEY = "key"
        private const val QUERY_PARAM_CITY = "q"
        private const val QUERY_PARAM_AQI = "aqi"
        private const val QUERY_PARAM_LANG = "lang"
        private const val QUERY_PARAM_ALERTS = "alerts"
        private const val QUERY_PARAM_QUANTITY_OF_DAYS = "days"
    }
}