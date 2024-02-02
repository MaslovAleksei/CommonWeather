package com.margarin.commonweather.presentation.screens.citylist

import com.margarin.commonweather.domain.City

sealed class CityListEvent {

    data object GetSavedCityList : CityListEvent()
    class DeleteSearchItem(val city: City) : CityListEvent()
    class AddSearchItem(val city: City) : CityListEvent()
}


