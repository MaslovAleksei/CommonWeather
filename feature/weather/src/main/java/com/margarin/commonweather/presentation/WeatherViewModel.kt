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
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val refreshDataUseCase: RefreshDataUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val state = _state.asStateFlow()

    internal fun send(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.RefreshWeatherEvent -> {
                viewModelScope.launch {
                    refreshDataUseCase(event.name)
                    getWeatherUseCase(event.name)
                        .onEach { _state.value = WeatherState.Error }
                        .filter { it.currentWeatherModel?.name?.isNotEmpty() == true }
                        .filter { it.byDaysWeatherModel?.size!! == 3 }
                        .onEach {
                            _state.value = WeatherState.Loading
                        }
                        .collect {
                            _state.value = WeatherState.WeatherInfo(it)
                            delay(200)
                            _state.value = WeatherState.Success
                            delay(1000)
                            _state.value = WeatherState.WeatherInfo(it)
                        }
                }
            }
        }
    }
}