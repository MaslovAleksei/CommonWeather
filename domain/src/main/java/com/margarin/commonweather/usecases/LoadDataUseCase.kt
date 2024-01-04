package com.margarin.commonweather.usecases

import com.margarin.commonweather.WeatherRepository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        location: String,
        lang: String
    ) = weatherRepository.loadData(location, lang)
}