package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.CityListFragment
import com.margarin.commonweather.ui.mainscreen.MainFragment
import com.margarin.commonweather.SearchFragment
import com.margarin.commonweather.database.AppDatabase
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
interface AppComponent: SearchComponent {

    fun inject(application: WeatherApp)
    fun inject(fragment: MainFragment)

    override fun injectCityListFragment(cityListFragment: CityListFragment)
    override fun injectSearchFragment(searchFragment: SearchFragment)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): AppComponent
    }
}