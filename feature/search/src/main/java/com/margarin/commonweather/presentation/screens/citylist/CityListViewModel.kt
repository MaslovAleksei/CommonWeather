package com.margarin.commonweather.presentation.screens.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.usecases.AddToFavouriteUseCase
import com.margarin.commonweather.domain.usecases.RemoveFromFavouritesUseCase
import com.margarin.commonweather.domain.usecases.GetSavedCityListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityListViewModel @Inject constructor(
    private val addSearchItemUseCase: AddToFavouriteUseCase,
    private val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase,
    private val getSavedCityListUseCase: GetSavedCityListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<CityListScreenState>(CityListScreenState.Initial)
    val state = _state.asStateFlow()

    internal fun sendEvent(event: CityListEvent) {
        when (event) {
            is CityListEvent.GetSavedCityList -> {
                viewModelScope.launch {
                    getSavedCityListUseCase()
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
                    removeFromFavouritesUseCase(event.city)
                }
            }
        }
    }
}