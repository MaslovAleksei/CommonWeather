package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.WeatherRepository
import javax.inject.Inject

class GetSearchItemUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(searchId: Int) = weatherRepository.getSearchItem(searchId)
}