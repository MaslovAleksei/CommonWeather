package com.margarin.commonweather.weather.usecases

import com.margarin.commonweather.weather.WeatherRepository
import javax.inject.Inject

class RefreshDataUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(location: String) = weatherRepository.refreshData(location)
}