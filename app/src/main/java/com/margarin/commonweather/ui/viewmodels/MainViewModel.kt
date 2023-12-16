package com.margarin.commonweather.ui.viewmodels

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

class MainViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getByDaysWeatherUseCase: GetByDaysWeatherUseCase,
    private val getByHoursWeatherUseCase: GetByHoursWeatherUseCase,
) : ViewModel() {

    private val _currentWeather = MutableLiveData<CurrentWeatherModel?>()
    val currentWeather: LiveData<CurrentWeatherModel?>
        get() = _currentWeather

    private var _byDaysWeather: LiveData<List<ByDaysWeatherModel>>? = null
    val byDaysWeather: LiveData<List<ByDaysWeatherModel>>?
        get() = _byDaysWeather

    private var _byHoursWeather: LiveData<List<ByHoursWeatherModel>>? = null
    val byHoursWeather: LiveData<List<ByHoursWeatherModel>>?
        get() = _byHoursWeather

    private val _location = MutableLiveData<String?>()
    val location: LiveData<String?>
        get() = _location

    fun initViewModel(name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            viewModelScope.launch(Dispatchers.IO) {
                loadDataUseCase(name)
            }.join()
            _currentWeather.value = getCurrentWeatherUseCase(name)
        }
    }

    fun setLocationValue(result: String) {
        _location.value = result
    }
}