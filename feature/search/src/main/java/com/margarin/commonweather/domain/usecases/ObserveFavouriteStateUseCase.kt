package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.SearchRepository
import javax.inject.Inject

class ObserveFavouriteStateUseCase  @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(cityId: Int) = repository.observeIsFavourite(cityId)
}