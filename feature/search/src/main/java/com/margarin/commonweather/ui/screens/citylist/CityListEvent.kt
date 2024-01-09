package com.margarin.commonweather.ui.screens.citylist

import com.google.android.gms.location.FusedLocationProviderClient
import com.margarin.commonweather.domain.SearchModel
import com.margarin.commonweather.utils.YandexMapManager
import com.yandex.mapkit.map.Map

sealed class CityListEvent {

    data object GetSavedCityList : CityListEvent()
    data object OpenMap : CityListEvent()
    class AddSearchItem(val searchModel: SearchModel) : CityListEvent()
    class DeleteSearchItem(val searchModel: SearchModel) : CityListEvent()
    class RequestSearchLocation(val query: String) : CityListEvent()
    class UseGps(
        val fusedLocationClient: FusedLocationProviderClient,
        val isMapGone: Boolean,
        val yandexMapManager: YandexMapManager,
        val map: Map
    ) : CityListEvent()
}


