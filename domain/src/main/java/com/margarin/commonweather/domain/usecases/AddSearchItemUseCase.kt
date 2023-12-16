package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.SearchModel
import javax.inject.Inject

class AddSearchItemUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(searchModel: SearchModel) =
        weatherRepository.addSearchItem(searchModel)
}