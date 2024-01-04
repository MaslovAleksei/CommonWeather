package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.ui.searchscreen.CityListFragment
import com.margarin.commonweather.ui.mainscreen.MainFragment
import com.margarin.commonweather.ui.searchscreen.SearchFragment
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
interface AppComponent {

    fun inject(application: WeatherApp)
    fun inject(fragment: MainFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: CityListFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}