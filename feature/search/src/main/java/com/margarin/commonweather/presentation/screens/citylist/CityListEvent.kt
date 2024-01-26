package com.margarin.commonweather.presentation.screens.citylist

import com.margarin.commonweather.domain.SearchModel

sealed class CityListEvent {

    data object GetSavedCityList : CityListEvent()
    class DeleteSearchItem(val searchModel: SearchModel) : CityListEvent()
    class AddSearchItem(val searchModel: SearchModel) : CityListEvent()
}


