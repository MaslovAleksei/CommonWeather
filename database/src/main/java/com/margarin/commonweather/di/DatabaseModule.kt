package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.database.AppDatabase
import com.margarin.commonweather.dao.ByDaysWeatherDao
import com.margarin.commonweather.dao.ByHoursWeatherDao
import com.margarin.commonweather.dao.CurrentWeatherDao
import com.margarin.commonweather.dao.SearchDao
import dagger.Module
import dagger.Provides

@Module
interface DatabaseModule {

    companion object {

        @Provides
        @AppScope
        fun provideCurrentWeatherDao(application: Application): CurrentWeatherDao {
            return AppDatabase.getInstance(application).currentWeatherDao()
        }

        @Provides
        @AppScope
        fun provideByDaysWeatherDao(application: Application): ByDaysWeatherDao {
            return AppDatabase.getInstance(application).byDaysWeatherDao()
        }

        @Provides
        @AppScope
        fun provideByHoursWeatherDao(application: Application): ByHoursWeatherDao {
            return AppDatabase.getInstance(application).byHoursWeatherDao()
        }

        @Provides
        @AppScope
        fun provideSearchDao(application: Application): SearchDao {
            return AppDatabase.getInstance(application).searchDao()
        }
    }
}