package com.margarin.commonweather.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.SearchModel
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.RequestSearchLocationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
) : ViewModel() {

    private val _requestLocation = MutableLiveData<List<SearchModel>?>()
    val requestLocation: LiveData<List<SearchModel>?>
        get() = _requestLocation

    fun addSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addSearchItemUseCase(searchModel)
        }
    }

    fun changeDefiniteLocation(query: String) {
        viewModelScope.launch {
            _requestLocation.value = requestSearchLocationUseCase(query)
        }
    }
}