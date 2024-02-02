package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.SearchRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke() = searchRepository.favouriteCities
}