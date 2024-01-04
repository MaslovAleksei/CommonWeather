package com.margarin.commonweather.usecases

import com.margarin.commonweather.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(name: String) = weatherRepository.getCurrentWeather(name)
}