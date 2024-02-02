package com.margarin.commonweather.search.usecases

import com.margarin.commonweather.search.City
import com.margarin.commonweather.search.SearchRepository
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(city: City) =
        searchRepository.addToFavourite(city)
}