package com.margarin.commonweather.presentation.screens.citylist

import com.margarin.commonweather.domain.SearchModel

sealed class CityListState {

    data object Initial : CityListState()
    data object EmptyList : CityListState()
    data object Map : CityListState()
    data object Locating : CityListState()
    class Content(val cityList: List<SearchModel>?) : CityListState()
}

