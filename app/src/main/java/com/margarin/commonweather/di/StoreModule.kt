package com.margarin.commonweather.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
interface StoreModule {

    companion object {

        @Provides
        fun provideStoreFactory(): StoreFactory = DefaultStoreFactory()
    }
}