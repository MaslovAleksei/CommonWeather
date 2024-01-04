package com.margarin.commonweather.di

import com.margarin.commonweather.AppScope
import com.margarin.commonweather.repository.SearchRepositoryImpl
import com.margarin.commonweather.repository.WeatherRepositoryImpl
import com.margarin.commonweather.SearchRepository
import com.margarin.commonweather.WeatherRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    @AppScope
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @AppScope
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}