package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.WeatherRepository
import javax.inject.Inject

class RefreshDataUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(location: String) = weatherRepository.refreshData(location)
}