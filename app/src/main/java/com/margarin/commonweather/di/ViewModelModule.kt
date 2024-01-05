package com.margarin.commonweather.di

import androidx.lifecycle.ViewModel
import com.margarin.commonweather.ui.SharedViewModel
import com.margarin.commonweather.ui.WeatherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    fun bindMainViewModel(viewModel: WeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    fun bindSearchViewModel(viewModel: SharedViewModel): ViewModel

}