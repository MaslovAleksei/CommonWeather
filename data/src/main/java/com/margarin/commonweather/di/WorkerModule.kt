package com.margarin.commonweather.di

import com.margarin.commonweather.weather.workers.CurrentWeatherNotificationWorker
import com.margarin.commonweather.weather.workers.RefreshWeatherWorker
import com.margarin.commonweather.weather.workers.factory.ChildWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(RefreshWeatherWorker::class)
    fun bindRefreshDataWorkerFactory(
        workerFactory: RefreshWeatherWorker.Factory
    ): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(CurrentWeatherNotificationWorker::class)
    fun bindCurrentWeatherNotificationWorker(
        workerFactory: CurrentWeatherNotificationWorker.Factory
    ): ChildWorkerFactory
}