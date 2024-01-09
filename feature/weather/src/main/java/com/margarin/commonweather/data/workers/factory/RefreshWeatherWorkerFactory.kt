package com.margarin.commonweather.data.workers.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.margarin.commonweather.data.workers.RefreshWeatherWorker
import javax.inject.Inject
import javax.inject.Provider

class RefreshWeatherWorkerFactory @Inject constructor(
    private val workerProviders: @JvmSuppressWildcards Map<Class<out ListenableWorker>, Provider<ChildWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            RefreshWeatherWorker::class.qualifiedName -> {
                val childWorkerFactory = workerProviders[RefreshWeatherWorker::class.java]?.get()
                return childWorkerFactory?.create(appContext, workerParameters)
            }
            else -> null
        }
    }
}