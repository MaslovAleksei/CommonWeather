package com.margarin.commonweather.data.di

import android.app.Application
import com.margarin.commonweather.data.repository.WeatherRepositoryImpl
import com.margarin.commonweather.data.database.AppDatabase
import com.margarin.commonweather.data.database.dao.SearchDao
import com.margarin.commonweather.data.database.dao.WeatherDao
import com.margarin.commonweather.data.remote.ApiFactory
import com.margarin.commonweather.data.remote.ApiService
import com.margarin.commonweather.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService{
            return ApiFactory.apiService
        }

        @Provides
        @ApplicationScope
        fun provideWeatherDao(application: Application): WeatherDao {
            return AppDatabase.getInstance(application).weatherDao()
        }

        @Provides
        @ApplicationScope
        fun provideSearchDao(application: Application): SearchDao {
            return AppDatabase.getInstance(application).searchDao()
        }
    }
}