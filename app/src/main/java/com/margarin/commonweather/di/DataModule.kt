package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.data.WeatherRepositoryImpl
import com.margarin.commonweather.data.database.AppDatabase
import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.database.dao.SearchDao
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