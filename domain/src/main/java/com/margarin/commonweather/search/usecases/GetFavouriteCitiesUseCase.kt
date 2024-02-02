package com.margarin.commonweather.search.usecases

import com.margarin.commonweather.search.SearchRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke() = searchRepository.favouriteCities
}