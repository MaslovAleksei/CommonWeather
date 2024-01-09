package com.margarin.commonweather.ui.screens.citylist

import com.margarin.commonweather.domain.SearchModel

sealed class CityListState {

    data object EmptyList : CityListState()
    data object OpenedMap : CityListState()
    data object Locating : CityListState()
    class Content(val cityList: List<SearchModel>?) : CityListState()
}

