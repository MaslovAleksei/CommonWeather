package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.SearchRepository
import javax.inject.Inject

class RequestSearchLocationUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String) = searchRepository.requestSearchLocation(query)
}