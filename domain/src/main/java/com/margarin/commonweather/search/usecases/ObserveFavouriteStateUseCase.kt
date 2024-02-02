package com.margarin.commonweather.search.usecases

import com.margarin.commonweather.search.SearchRepository
import javax.inject.Inject

class ObserveFavouriteStateUseCase  @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(cityId: Int) = repository.observeIsFavourite(cityId)
}