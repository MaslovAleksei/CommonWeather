package com.margarin.commonweather.presentation.screens.search

import com.margarin.commonweather.domain.City

sealed class SearchScreenState {

    data object Initial: SearchScreenState()
    data object Close: SearchScreenState()
    data object Locating : SearchScreenState()
    class SearchesList(val queryList: List<City>?) : SearchScreenState()

}
