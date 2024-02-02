package com.margarin.commonweather.presentation.screens.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityListViewModel @Inject constructor(
    private val addSearchItemUseCase: com.margarin.commonweather.search.usecases.AddToFavouriteUseCase,
    private val removeFromFavouritesUseCase: com.margarin.commonweather.search.usecases.RemoveFromFavouritesUseCase,
    private val getFavouriteCitiesUseCase: com.margarin.commonweather.search.usecases.GetFavouriteCitiesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<CityListScreenState>(CityListScreenState.Initial)
    val state = _state.asStateFlow()

    internal fun sendEvent(event: CityListEvent) {
        when (event) {
            is CityListEvent.GetSavedCityList -> {
                viewModelScope.launch {
                    getFavouriteCitiesUseCase()
                        .onEach { _state.value = CityListScreenState.Content(cityList = it) }
                        .filter { it.isEmpty() }
                        .collect {
                            _state.value = CityListScreenState.EmptyList
                        }
                }
            }

            is CityListEvent.AddSearchItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addSearchItemUseCase(event.city)
                }
            }

            is CityListEvent.DeleteSearchItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    removeFromFavouritesUseCase(event.city.id)
                }
            }
        }
    }
}