package com.margarin.commonweather.ui.viewmodels

import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getByDaysWeatherUseCase: GetByDaysWeatherUseCase,
    private val getByHoursWeatherUseCase: GetByHoursWeatherUseCase,
) : ViewModel() {

    private var _currentWeather: LiveData<CurrentWeatherModel>? = null
    val currentWeather: LiveData<CurrentWeatherModel>?
        get() = _currentWeather

    private var _byDaysWeather: LiveData<List<ByDaysWeatherModel>>? = null
    val byDaysWeather: LiveData<List<ByDaysWeatherModel>>?
        get() = _byDaysWeather

    private var _byHoursWeather: LiveData<List<ByHoursWeatherModel>>? = null
    val byHoursWeather: LiveData<List<ByHoursWeatherModel>>?
        get() = _byHoursWeather

    fun initViewModel(location: String) {
        viewModelScope.launch {
            runBlocking {
                loadDataFromApi(location)
                delay(1000)
            }
                _currentWeather = getCurrentWeatherUseCase()
                _byDaysWeather = getByDaysWeatherUseCase()
                _byHoursWeather = getByHoursWeatherUseCase()

        }

    }

    fun loadDataFromApi(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadDataUseCase(location)
        }
    }
}