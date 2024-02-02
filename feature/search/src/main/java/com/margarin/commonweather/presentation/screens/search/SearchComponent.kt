package com.margarin.commonweather.presentation.screens.search

import com.margarin.commonweather.search.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(query: String)

    fun onClickBack()

    fun onClickDefineLocation()

    fun onClickCity(city: City)

    fun onClickAddToFavourite(city: City)

    fun onStopSearch()

}