package com.margarin.commonweather.usecases

import com.margarin.commonweather.SearchRepository
import com.margarin.commonweather.models.SearchModel
import javax.inject.Inject

class AddSearchItemUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchModel: SearchModel) =
        searchRepository.addSearchItem(searchModel)
}