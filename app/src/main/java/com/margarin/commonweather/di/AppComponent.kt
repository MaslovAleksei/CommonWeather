package com.margarin.commonweather.di

import android.content.Context
import com.margarin.commonweather.AppScope
import com.margarin.commonweather.MainActivity
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        DataModule::class,
        WorkerModule::class,
        StoreModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}