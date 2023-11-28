package com.margarin.commonweather.app

import android.app.Application
import com.margarin.commonweather.di.DaggerApplicationComponent

class WeatherApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}