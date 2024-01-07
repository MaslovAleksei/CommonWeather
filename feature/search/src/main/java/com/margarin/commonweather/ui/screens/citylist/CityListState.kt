package com.margarin.commonweather.ui.screens.citylist

import com.margarin.commonweather.domain.SearchModel

sealed class CityListState

data object Loading: CityListState()
data object EmptyList: CityListState()
class CityList(val cityList: List<SearchModel>?): CityListState()

