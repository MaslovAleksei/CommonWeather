package com.margarin.commonweather.di

import com.margarin.commonweather.Feature
import com.margarin.commonweather.ui.WeatherFragment

@Feature
interface WeatherComponent {

    fun injectWeatherFragment(weatherFragment: WeatherFragment)

}
