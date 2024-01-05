package com.margarin.commonweather.ui.viewmodels

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.dataStore
import com.margarin.commonweather.domain.SearchModel
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.RequestSearchLocationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val application: Application
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

    fun saveToDataStore(name: String) {
        val dataStoreKey = stringPreferencesKey(LOCATION)
        runBlocking {
            application.dataStore.edit { settings ->
                settings[dataStoreKey] = name
            }
        }
    }
}