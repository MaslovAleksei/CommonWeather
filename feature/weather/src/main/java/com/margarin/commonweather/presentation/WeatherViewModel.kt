package com.margarin.commonweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.usecases.GetWeatherUseCase
import com.margarin.commonweather.domain.usecases.RefreshDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val refreshDataUseCase: RefreshDataUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Initial)
    val state = _state.asStateFlow()

    internal fun sendEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.RefreshWeather -> {
                viewModelScope.launch {
                    refreshDataUseCase(event.name)
                    getWeatherUseCase(event.name)
                        .onStart { _state.value = WeatherState.Loading }
                        .onEach { _state.value = WeatherState.Error }
                        .filter { it.currentWeatherModel?.name?.isNotEmpty() == true }
                        .filter { it.byDaysWeatherModel?.size!! == 3 }
                        .onEach {
                            _state.value = WeatherState.Loading
                        }
                        .collect {
                            _state.value = WeatherState.Info(it)
                            delay(200)
                            _state.value = WeatherState.Success
                            delay(1000)
                            _state.value = WeatherState.Info(it)
                        }
                }
            }
        }
    }
}