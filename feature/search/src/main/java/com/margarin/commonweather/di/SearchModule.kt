package com.margarin.commonweather.di

import com.margarin.commonweather.AppScope
import com.margarin.commonweather.data.SearchRepositoryImpl
import com.margarin.commonweather.domain.SearchRepository
import dagger.Binds
import dagger.Module

@Module
interface SearchModule {

    @Binds
    @AppScope
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}