package com.margarin.commonweather.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.usecases.LoadByDaysWeatherUseCase
import com.margarin.commonweather.domain.usecases.LoadByHoursWeatherUseCase
import com.margarin.commonweather.domain.usecases.LoadCurrentWeatherUseCase
import com.margarin.commonweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val loadCurrentWeatherUseCase: LoadCurrentWeatherUseCase,
    private val loadByDaysWeatherUseCase: LoadByDaysWeatherUseCase,
    private val loadByHoursWeatherUseCase: LoadByHoursWeatherUseCase
) : ViewModel() {

    var currentWeather: LiveData<CurrentWeatherModel>? = loadCurrentWeatherUseCase()


    fun loadDataFromApi(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadDataUseCase(city)
        }
    }




}