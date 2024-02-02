package com.margarin.commonweather.presentation.screens.citylist

import com.margarin.commonweather.search.City

sealed class CityListScreenState {

    data object Initial : CityListScreenState()
    data object EmptyList : CityListScreenState()
    class Content(val cityList: List<City>?) : CityListScreenState()
}

