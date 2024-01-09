package com.margarin.commonweather.di

import com.margarin.commonweather.data.workers.factory.ChildWorkerFactory
import com.margarin.commonweather.data.workers.RefreshWeatherWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(RefreshWeatherWorker::class)
    fun bindRefreshDataWorkerFactory(workerFactory: RefreshWeatherWorker.Factory): ChildWorkerFactory
}