package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.SearchRepository
import com.margarin.commonweather.domain.models.SearchModel
import javax.inject.Inject

class AddSearchItemUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchModel: SearchModel) =
        searchRepository.addSearchItem(searchModel)
}