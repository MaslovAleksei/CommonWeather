package com.margarin.commonweather

import android.app.Application
import androidx.work.Configuration
import com.margarin.commonweather.di.AppComponent
import com.margarin.commonweather.di.DaggerAppComponent
import com.margarin.commonweather.weather.workers.factory.WeatherWorkerFactory
import javax.inject.Inject

class WeatherApp : Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: WeatherWorkerFactory

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}