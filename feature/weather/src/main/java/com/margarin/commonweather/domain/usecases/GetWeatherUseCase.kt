package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.WeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(name: String): Flow<WeatherModel> {
        return flow { emit(weatherRepository.getWeather(name)) }
    }
}
