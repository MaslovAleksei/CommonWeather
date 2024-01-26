package com.margarin.commonweather.presentation.screens.search

import com.google.android.gms.location.FusedLocationProviderClient
import com.margarin.commonweather.domain.SearchModel

sealed class SearchEvent {

    class AddSearchItem(val searchModel: SearchModel) : SearchEvent()
    class OnQuery(val query: String) : SearchEvent()
    class UseGps(val fusedLocationClient: FusedLocationProviderClient) : SearchEvent()
}

