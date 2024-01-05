package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.domain.SearchRepository
import com.margarin.commonweather.domain.SearchModel
import javax.inject.Inject

class DeleteSearchItemUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchModel: SearchModel) = searchRepository.deleteSearchItem(searchModel)
}