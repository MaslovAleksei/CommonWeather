package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.dao.SearchDao
import com.margarin.commonweather.dao.WeatherDao
import com.margarin.commonweather.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
interface DatabaseModule {

    companion object {

        @Provides
        @AppScope
        fun provideCurrentWeatherDao(application: Application): WeatherDao {
            return AppDatabase.getInstance(application).weatherDao()
        }

        @Provides
        @AppScope
        fun provideSearchDao(application: Application): SearchDao {
            return AppDatabase.getInstance(application).searchDao()
        }
    }
}