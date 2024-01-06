package com.margarin.commonweather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.models.WeatherModel
import com.margarin.commonweather.domain.usecases.GetWeatherUseCase
import com.margarin.commonweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _weather = MutableLiveData<WeatherModel?>()
    val weather: LiveData<WeatherModel?>
        get() = _weather

    fun initViewModel(name: String, lang: String) {
        viewModelScope.launch(Dispatchers.Main) {
            viewModelScope.launch(Dispatchers.IO) {
                loadDataUseCase(name, lang)
            }.join()
            _weather.value = getWeatherUseCase(name)
        }
    }
}