package com.margarin.commonweather.di

import com.margarin.commonweather.AppScope
import com.margarin.commonweather.data.WeatherRepositoryImpl
import com.margarin.commonweather.domain.WeatherRepository
import dagger.Binds
import dagger.Module

@Module
interface WeatherModule {

    @Binds
    @AppScope
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

}