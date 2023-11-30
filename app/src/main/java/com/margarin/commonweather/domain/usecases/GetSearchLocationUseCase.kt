package com.margarin.commonweather.domain.usecases

import androidx.lifecycle.LiveData
import com.margarin.commonweather.domain.WeatherRepository
import com.margarin.commonweather.domain.models.SearchModel
import javax.inject.Inject

class GetSearchLocationUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(query: String) = weatherRepository.getSearchLocation(query)
}