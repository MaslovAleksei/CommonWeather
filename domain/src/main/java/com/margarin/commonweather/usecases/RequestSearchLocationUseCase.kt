package com.margarin.commonweather.usecases

import com.margarin.commonweather.SearchRepository
import javax.inject.Inject

class RequestSearchLocationUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String) = searchRepository.requestSearchLocation(query)
}