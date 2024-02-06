package com.margarin.commonweather.di

import android.content.Context
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.local.dao.SearchDao
import com.margarin.commonweather.local.dao.WeatherDao
import com.margarin.commonweather.local.database.AppDatabase
import com.margarin.commonweather.remote.ApiFactory
import com.margarin.commonweather.remote.ApiService
import com.margarin.commonweather.search.SearchRepository
import com.margarin.commonweather.search.SearchRepositoryImpl
import com.margarin.commonweather.weather.WeatherRepository
import com.margarin.commonweather.weather.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @AppScope
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @AppScope
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @Provides
        @AppScope
        fun provideCurrentWeatherDao(context: Context): WeatherDao {
            return AppDatabase.getInstance(context).weatherDao()
        }

        @Provides
        @AppScope
        fun provideSearchDao(context: Context): SearchDao {
            return AppDatabase.getInstance(context).searchDao()
        }

        @Provides
        @AppScope
        fun provideApiService(): ApiService = ApiFactory.apiService
    }
}