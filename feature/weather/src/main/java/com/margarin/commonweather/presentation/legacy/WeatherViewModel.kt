package com.margarin.commonweather.presentation.legacy//package com.margarin.commonweather.presentation
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.filter
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//class WeatherViewModel @Inject constructor(
//    private val refreshDataUseCase: com.margarin.commonweather.weather.usecases.RefreshDataUseCase,
//    private val getWeatherUseCase: com.margarin.commonweather.weather.usecases.GetWeatherUseCase
//) : ViewModel() {
//
//    private val _state = MutableStateFlow<WeatherState>(WeatherState.Initial)
//    val state = _state.asStateFlow()
//
//    internal fun sendEvent(event: WeatherEvent) {
//        when (event) {
//            is WeatherEvent.RefreshWeather -> {
//                viewModelScope.launch {
//                    refreshDataUseCase(event.name)
//                    getWeatherUseCase(event.name)
//                        .onStart { _state.value = WeatherState.Loading }
//                        .onEach { _state.value = WeatherState.Error }
//                        .filter { it.currentWeatherModel?.name?.isNotEmpty() == true }
//                        .filter { it.byDaysWeatherModel?.size!! == 3 }
//                        .onEach {
//                            _state.value = WeatherState.Loading
//                        }
//                        .collect {
//                            _state.value = WeatherState.Info(it)
//                            delay(200)
//                            _state.value = WeatherState.Success
//                            delay(1000)
//                            _state.value = WeatherState.Info(it)
//                        }
//                }
//            }
//        }
//    }
//}