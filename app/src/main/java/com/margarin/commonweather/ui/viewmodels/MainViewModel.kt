package com.margarin.commonweather.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.commonweather.domain.usecases.GetSearchLocationUseCase
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
    private val loadByHoursWeatherUseCase: LoadByHoursWeatherUseCase,
    private val getSearchLocationUseCase: GetSearchLocationUseCase
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

    private val _searchLocation = MutableLiveData<List<SearchModel>?>()
    val searchLocation: LiveData<List<SearchModel>?>
        get() = _searchLocation

    fun initViewModel(location: String) {
        loadDataFromApi(location)
        Thread.sleep(3000)
        _currentWeather = loadCurrentWeatherUseCase()
        _byDaysWeather = loadByDaysWeatherUseCase()
        _byHoursWeather = loadByHoursWeatherUseCase()
    }

    fun loadDataFromApi(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadDataUseCase(location)
        }
    }

    fun getSearchLocation(query: String) {
        viewModelScope.launch {
            _searchLocation.value = getSearchLocationUseCase(query)
        }

    }
}