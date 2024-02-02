package com.margarin.commonweather

import android.app.Application
import androidx.work.Configuration
import com.margarin.commonweather.di.DaggerAppComponent
import com.margarin.commonweather.di.SearchComponen
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.di.WeatherComponen
import com.margarin.commonweather.di.WeatherComponentProvider
import com.margarin.commonweather.weather.workers.factory.WeatherWorkerFactory
import javax.inject.Inject

class WeatherApp : Application(), SearchComponentProvider, WeatherComponentProvider,
    Configuration.Provider {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    @Inject
    lateinit var workerFactory: WeatherWorkerFactory

    override fun onCreate() {
        appComponent.inject(this)
        super.onCreate()
    }

    override fun getSearchComponent(): SearchComponen {
        return appComponent
    }

    override fun getWeatherComponent(): WeatherComponen {
        return appComponent
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}