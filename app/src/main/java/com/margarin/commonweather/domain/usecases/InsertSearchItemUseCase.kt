package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.SearchModel
import javax.inject.Inject

class InsertSearchItemUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        searchModel: SearchModel
    ) = weatherRepository.insertSearchItem(searchModel)
}