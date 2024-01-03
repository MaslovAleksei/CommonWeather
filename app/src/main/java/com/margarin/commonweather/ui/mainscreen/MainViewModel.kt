package com.margarin.commonweather.ui.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.models.WeatherModel
import com.margarin.commonweather.domain.usecases.GetWeatherModelUseCase
import com.margarin.commonweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val getWeatherModelUseCase: GetWeatherModelUseCase,
) : ViewModel() {

    private val _weather = MutableLiveData<WeatherModel?>()
    val weather: LiveData<WeatherModel?>
        get() = _weather

    private val _location = MutableLiveData<String?>()
    val location: LiveData<String?>
        get() = _location

    fun initViewModel(name: String, lang: String) {
        viewModelScope.launch(Dispatchers.Main) {
            viewModelScope.launch(Dispatchers.IO) {
                loadDataUseCase(name, lang)
            }.join()
            _weather.value = getWeatherModelUseCase(name)
        }
    }

    fun setLocationValue(result: String) {
        _location.value = result
    }
}