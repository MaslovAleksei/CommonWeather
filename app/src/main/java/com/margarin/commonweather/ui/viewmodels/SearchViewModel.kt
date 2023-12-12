package com.margarin.commonweather.ui.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.DeleteSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSearchListUseCase
import com.margarin.commonweather.domain.usecases.GetSearchLocationUseCase
import com.margarin.commonweather.ui.screens.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getSearchLocationUseCase: GetSearchLocationUseCase,
    private val getSearchItemUseCase: GetSearchItemUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val deleteSearchItemUseCase: DeleteSearchItemUseCase,
    private val getSearchListUseCase: GetSearchListUseCase,
    private val mainViewModel: MainViewModel,
    private val application: Application
) : ViewModel() {

    private val _searchLocation =
        MutableLiveData<List<SearchModel>?>()
    val searchLocation: LiveData<List<SearchModel>?>
        get() = _searchLocation

    private var _searchList: LiveData<List<SearchModel>>? =
        null
    val searchList: LiveData<List<SearchModel>>?
        get() = _searchList

    private val _searchItem =
        MutableLiveData<SearchModel?>()
    val searchItem: LiveData<SearchModel?>
        get() = _searchItem

    fun getSearchLocation(query: String) {
        viewModelScope.launch {
            _searchLocation.value = getSearchLocationUseCase(query)
        }
    }

    fun loadSearchList() {
        viewModelScope.launch {
            _searchList = getSearchListUseCase()
        }
    }

    fun addSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addSearchItemUseCase(searchModel)
        }
    }

    fun deleteSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSearchItemUseCase(searchModel)
        }
    }

    fun getSearchItem(searchId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _searchItem.value = getSearchItemUseCase(searchId)
        }
    }

    fun changeSearchItem(city: String) {
        runBlocking {
            val dataStoreKey = stringPreferencesKey(LOCATION)
            application.dataStore.edit { settings ->
                settings[dataStoreKey] = city
            }
        }
        mainViewModel.loadDataFromApi(city)
    }

    fun changeIsMenuShown(searchModel: SearchModel) {
        viewModelScope.launch {
            val newItem = searchModel.copy(isMenuShown = !searchModel.isMenuShown)
            addSearchItemUseCase(newItem)
        }
    }

    fun getLocation(fusedLocationClient: FusedLocationProviderClient) {
        var locationLatLon: String
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                locationLatLon = "${it.result.latitude}, ${it.result.longitude}"
                changeSearchItem(locationLatLon)
            }

    }

    fun isLocationEnabled(): Boolean {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }

    companion object {

        private const val LOCATION = "location"
        private const val UNDEFINED_LOCATION = "undefined_location"
    }
}