package com.margarin.commonweather.di

import androidx.lifecycle.ViewModel
import com.margarin.commonweather.ui.viewmodels.MainViewModel
import com.margarin.commonweather.ui.viewmodels.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

}