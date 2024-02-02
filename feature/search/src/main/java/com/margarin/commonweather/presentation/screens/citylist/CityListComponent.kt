package com.margarin.commonweather.presentation.screens.citylist

import com.margarin.commonweather.search.City
import kotlinx.coroutines.flow.StateFlow

interface CityListComponent {

    val model: StateFlow<CityListStore.State>

    fun onClickBack()

    fun onClickSearch()

    fun onSwipeDelete()

    fun onClickCityItem(city: City)
}