package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.WeatherRepository
import javax.inject.Inject

class LoadByDaysWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke() = weatherRepository.loadByDaysWeather()

}