package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.SearchRepository
import javax.inject.Inject

class GetSavedCityListUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() = searchRepository.getSavedCityList()
}