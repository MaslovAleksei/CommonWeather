package com.margarin.commonweather

import android.app.Application
import androidx.work.Configuration
import com.margarin.commonweather.data.workers.factory.WeatherWorkerFactory
import com.margarin.commonweather.di.DaggerAppComponent
import com.margarin.commonweather.di.SearchComponent
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.di.WeatherComponent
import com.margarin.commonweather.di.WeatherComponentProvider
import com.yandex.mapkit.MapKitFactory
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
        MapKitFactory.setApiKey("50afcc11-65c1-47b2-aa6e-92e49af0f348")
        //TODO replace api key to local.properties
    }

    override fun getSearchComponent(): SearchComponent {
        return appComponent
    }

    override fun getWeatherComponent(): WeatherComponent {
        return appComponent
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}