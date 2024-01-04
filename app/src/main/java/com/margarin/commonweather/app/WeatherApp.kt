package com.margarin.commonweather.app

import android.app.Application
import com.margarin.commonweather.di.DaggerAppComponent
import com.yandex.mapkit.MapKitFactory

class WeatherApp: Application() {

    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
        MapKitFactory.setApiKey("50afcc11-65c1-47b2-aa6e-92e49af0f348")
        //TODO replace api key to local.properties
    }
}