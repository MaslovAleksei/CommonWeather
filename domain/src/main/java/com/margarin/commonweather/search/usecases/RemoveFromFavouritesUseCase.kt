package com.margarin.commonweather.search.usecases

import com.margarin.commonweather.search.SearchRepository
import javax.inject.Inject

class RemoveFromFavouritesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(cityId: Int) = searchRepository.removeFromFavourite(cityId)
}