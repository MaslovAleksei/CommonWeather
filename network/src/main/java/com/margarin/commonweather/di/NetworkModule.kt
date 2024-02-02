package com.margarin.commonweather.di

import com.margarin.commonweather.ApiFactory
import com.margarin.commonweather.ApiService
import com.margarin.commonweather.AppScope
import dagger.Module
import dagger.Provides

@Module
interface NetworkModule {

    companion object {
        @Provides
        @AppScope
        fun provideApiService(): ApiService = ApiFactory.apiService
    }
}