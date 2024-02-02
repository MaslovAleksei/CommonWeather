package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.WeatherApp
import com.margarin.commonweather.presentation.WeatherFragment
import com.margarin.commonweather.presentation.screens.citylist.CityListFragment
import com.margarin.commonweather.presentation.screens.search.SearchFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        ViewModelModule::class,
        DataModule::class,
        WorkerModule::class
    ]
)
interface AppComponent : SearchComponen, WeatherComponen {

    fun inject(application: WeatherApp)

    override fun injectCityListFragment(cityListFragment: CityListFragment)
    override fun injectSearchFragment(searchFragment: SearchFragment)
    override fun injectWeatherFragment(weatherFragment: WeatherFragment)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): AppComponent
    }
}