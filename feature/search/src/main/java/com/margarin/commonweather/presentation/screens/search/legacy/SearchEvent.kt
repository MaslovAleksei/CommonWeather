package com.margarin.commonweather.presentation.screens.search.legacy//package com.margarin.commonweather.presentation.screens.search
//
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.margarin.commonweather.search.City
//
//sealed class SearchEvent {
//
//    class AddSearchItem(val city: City) : SearchEvent()
//    class OnQuery(val query: String) : SearchEvent()
//    class UseGps(val fusedLocationClient: FusedLocationProviderClient) : SearchEvent()
//}
//