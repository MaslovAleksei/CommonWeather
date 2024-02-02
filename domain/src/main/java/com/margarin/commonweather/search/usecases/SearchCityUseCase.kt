package com.margarin.commonweather.search.usecases

import com.margarin.commonweather.search.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String) = searchRepository.search(query)
}