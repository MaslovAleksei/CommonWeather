package com.margarin.commonweather.domain.usecases

import androidx.lifecycle.LiveData
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import javax.inject.Inject

class LoadCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke() : LiveData<CurrentWeatherModel> = weatherRepository.loadCurrentWeather()

}