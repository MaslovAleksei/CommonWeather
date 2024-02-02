package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.SearchRepository
import javax.inject.Inject

class RemoveFromFavouritesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(cityId: Int) = searchRepository.removeFromFavourite(cityId)
}