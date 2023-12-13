package com.margarin.commonweather.ui.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.widget.Toast
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
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
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

    fun isGpsEnabled(
        fusedLocationClient: FusedLocationProviderClient,
        isMapGone: Boolean,
        map: Map
    ) {
        val locationManager =
            application
                .getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            getLocation(fusedLocationClient, isMapGone, map)
        } else {
            application.startActivity(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    private fun getLocation(
        fusedLocationClient: FusedLocationProviderClient,
        isMapGone: Boolean,
        map: Map
    ) {
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
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener {
                if (isMapGone) {
                    val locationLatLon = "${it.result.latitude}, ${it.result.longitude}"
                    changeSearchItem(locationLatLon)
                } else {
                    map.move(
                        CameraPosition(
                            Point(it.result.latitude, it.result.longitude),
                            6.0f,
                            0.0f,
                            0.0f
                        ),
                        LINEAR_ANIMATION
                    ) {}
                }
            }

    }

    fun changeZoomByStep(value: Float, map: Map) {
        with(map.cameraPosition) {
            map.move(
                CameraPosition(target, zoom + value, azimuth, tilt),
                SMOOTH_ANIMATION,
                null,
            )
        }
    }

    fun configureMap(map: Map) {
        MapKitFactory.initialize(application)
        map.move(START_POSITION, LINEAR_ANIMATION) {
            Toast.makeText(
                application,
                "Move the center of the screen to the desired location and click on the " +
                        "\"save point\" button",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {

        private const val LOCATION = "location"
        private const val UNDEFINED_LOCATION = "undefined_location"

        private val LINEAR_ANIMATION = Animation(Animation.Type.LINEAR, 1f)
        private val SMOOTH_ANIMATION = Animation(Animation.Type.SMOOTH, 0.4f)
        private val START_POSITION = CameraPosition(
            Point(55.0, 50.0),
            4.0f,
            0.0f,
            0.0f
        )
    }
}