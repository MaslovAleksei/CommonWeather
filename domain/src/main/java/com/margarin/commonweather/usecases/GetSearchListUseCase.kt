package com.margarin.commonweather.usecases

import com.margarin.commonweather.SearchRepository
import javax.inject.Inject

class GetSearchListUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke() = searchRepository.getSearchList()
}