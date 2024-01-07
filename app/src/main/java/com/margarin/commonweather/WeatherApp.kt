package com.margarin.commonweather

import android.app.Application
import com.margarin.commonweather.di.DaggerAppComponent
import com.margarin.commonweather.di.SearchComponent
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.di.WeatherComponent
import com.margarin.commonweather.di.WeatherComponentProvider
import com.yandex.mapkit.MapKitFactory

class WeatherApp: Application(), SearchComponentProvider, WeatherComponentProvider {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

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
}