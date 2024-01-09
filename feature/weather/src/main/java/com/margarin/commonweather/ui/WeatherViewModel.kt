package com.margarin.commonweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.usecases.GetWeatherUseCase
import com.margarin.commonweather.domain.usecases.RefreshDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val refreshDataUseCase: RefreshDataUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val state = _state.asStateFlow()

    fun send(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.RefreshWeatherEvent -> {
                viewModelScope.launch(Dispatchers.Main) {
                    viewModelScope.launch(Dispatchers.IO) {
                        refreshDataUseCase(event.name)
                    }.join()
                    val weather = getWeatherUseCase(event.name)
                    if (weather == null ||
                        weather.currentWeatherModel?.name?.isEmpty() == true ||
                        weather.byDaysWeatherModel?.size!! == 0
                    ) {
                        _state.value = WeatherState.Error
                        return@launch
                    }
                    _state.value = WeatherState.WeatherInfo(weather)
                    delay(200)
                    _state.value = WeatherState.Success
                    delay(1000)
                    _state.value = WeatherState.WeatherInfo(weather)
                }
            }
        }
    }
}