package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.ui.CityListFragment
import com.margarin.commonweather.ui.WeatherFragment
import com.margarin.commonweather.ui.SearchFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent: SearchComponent, WeatherComponent {

    fun inject(application: WeatherApp)

    override fun injectCityListFragment(cityListFragment: CityListFragment)
    override fun injectSearchFragment(searchFragment: SearchFragment)
    override fun injectWeatherFragment(weatherFragment: WeatherFragment)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): AppComponent
    }
}