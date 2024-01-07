package com.margarin.commonweather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.usecases.GetWeatherUseCase
import com.margarin.commonweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableLiveData<WeatherState>()
    val state: LiveData<WeatherState>
        get() = _state

    fun send(event: WeatherEvent) {
        when (event) {
            is LoadWeatherEvent -> {
                loadWeatherData(name = event.name, lang = event.lang)
            }
        }
    }

    private fun loadWeatherData(name: String, lang: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = Loading

            viewModelScope.launch(Dispatchers.IO) {
                loadDataUseCase(name, lang)
            }.join()
            val weather = getWeatherUseCase(name)
            if (weather == null ||
                weather.currentWeatherModel?.name?.isEmpty() == true ||
                weather.byDaysWeatherModel?.size!! == 0 ||
                weather.byHoursWeatherModel?.size!! == 0
            ) {
                _state.value = Error
                return@launch
            }
            delay(200)
            _state.value = Success
            delay(700)
            _state.value = WeatherInfo(weather)
        }
    }
}