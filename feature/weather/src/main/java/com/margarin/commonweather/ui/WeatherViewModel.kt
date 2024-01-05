package com.margarin.commonweather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.usecases.GetByDaysWeatherUseCase
import com.margarin.commonweather.domain.usecases.GetByHoursWeatherUseCase
import com.margarin.commonweather.domain.usecases.GetCurrentWeatherUseCase
import com.margarin.commonweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getByDaysWeatherUseCase: GetByDaysWeatherUseCase,
    private val getByHoursWeatherUseCase: GetByHoursWeatherUseCase,
) : ViewModel() {

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    val currentWeather: LiveData<CurrentWeatherModel?>
        get() = _currentWeather

    private val _byDaysWeather = MutableLiveData<List<ByDaysWeatherModel>?>()
    val byDaysWeather: LiveData<List<ByDaysWeatherModel>?>
        get() = _byDaysWeather

    private val _byHoursWeather = MutableLiveData<List<ByHoursWeatherModel>?>()
    val byHoursWeather: LiveData<List<ByHoursWeatherModel>?>
        get() = _byHoursWeather

    private val _location = MutableLiveData<String?>()
    val location: LiveData<String?>
        get() = _location

    fun initViewModel(name: String, lang: String) {
        viewModelScope.launch(Dispatchers.Main) {
            viewModelScope.launch(Dispatchers.IO) {
                loadDataUseCase(name, lang)
            }.join()
            _currentWeather.value = getCurrentWeatherUseCase(name)
            _byDaysWeather.value = getByDaysWeatherUseCase(name)
            _byHoursWeather.value = getByHoursWeatherUseCase(name)
        }
    }

    fun setLocationValue(result: String) {
        _location.value = result
    }
}