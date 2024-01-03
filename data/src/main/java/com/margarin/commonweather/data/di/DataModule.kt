package com.margarin.commonweather.data.di

import android.app.Application
import com.margarin.commonweather.data.repository.WeatherRepositoryImpl
import com.margarin.commonweather.data.database.AppDatabase
import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.database.dao.SearchDao
import com.margarin.commonweather.data.api.ApiFactory
import com.margarin.commonweather.data.api.ApiService
import com.margarin.commonweather.data.repository.SearchRepositoryImpl
import com.margarin.commonweather.domain.SearchRepository
import com.margarin.commonweather.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @ApplicationScope
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService{
            return ApiFactory.apiService
        }

        @Provides
        @ApplicationScope
        fun provideCurrentWeatherDao(application: Application): CurrentWeatherDao {
            return AppDatabase.getInstance(application).currentWeatherDao()
        }

        @Provides
        @ApplicationScope
        fun provideByDaysWeatherDao(application: Application): ByDaysWeatherDao {
            return AppDatabase.getInstance(application).byDaysWeatherDao()
        }

        @Provides
        @ApplicationScope
        fun provideByHoursWeatherDao(application: Application): ByHoursWeatherDao {
            return AppDatabase.getInstance(application).byHoursWeatherDao()
        }

        @Provides
        @ApplicationScope
        fun provideSearchDao(application: Application): SearchDao {
            return AppDatabase.getInstance(application).searchDao()
        }
    }
}