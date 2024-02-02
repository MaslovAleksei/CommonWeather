package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.City
import com.margarin.commonweather.domain.SearchRepository
import javax.inject.Inject

class RemoveFromFavouritesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(city: City) = searchRepository.removeFromFavourite(city)
}