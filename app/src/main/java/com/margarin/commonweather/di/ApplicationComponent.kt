package com.margarin.commonweather.di

import android.app.Application
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.ui.screens.MainFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class

    ]
)
interface ApplicationComponent {

    fun inject(application: WeatherApp)
    fun inject(fragment: MainFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}