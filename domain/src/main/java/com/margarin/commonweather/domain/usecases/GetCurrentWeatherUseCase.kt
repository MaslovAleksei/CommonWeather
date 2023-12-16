package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(name: String) = weatherRepository.getCurrentWeather(name)
}