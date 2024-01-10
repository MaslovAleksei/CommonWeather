package com.margarin.commonweather.di

import androidx.lifecycle.ViewModel
import com.margarin.commonweather.presentation.screens.citylist.CityListViewModel
import com.margarin.commonweather.presentation.screens.search.SearchViewModel
import com.margarin.commonweather.presentation.WeatherViewModel
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
    @ViewModelKey(CityListViewModel::class)
    fun bindCityListViewModel(viewModel: CityListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

}