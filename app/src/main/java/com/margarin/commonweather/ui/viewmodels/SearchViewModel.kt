package com.margarin.commonweather.ui.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
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
import com.margarin.commonweather.domain.usecases.GetSearchListUseCase
import com.margarin.commonweather.domain.usecases.RequestSearchLocationUseCase
import com.margarin.commonweather.ui.screens.dataStore
import com.margarin.commonweather.utils.LOCATION
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
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val deleteSearchItemUseCase: DeleteSearchItemUseCase,
    private val getSearchListUseCase: GetSearchListUseCase,
    private val application: Application
) : ViewModel() {

    private val _definiteLocation = MutableLiveData<List<SearchModel>?>()
    val definiteLocation: LiveData<List<SearchModel>?>
        get() = _definiteLocation

    private val _savedLocation = MutableLiveData<List<SearchModel>?>()
    val savedLocation: LiveData<List<SearchModel>?>
        get() = _savedLocation

    private var _searchList: LiveData<List<SearchModel>>? = null
    val searchList: LiveData<List<SearchModel>>?
        get() = _searchList

    fun getSearchList() {
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

    fun isGpsEnabled(): Boolean {
        val locationManager =
            application
                .getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }

    fun getLocationLatLon(fusedLocationClient: FusedLocationProviderClient) {
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { }
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener {
                changeDefiniteLocation("${it.result.latitude}, ${it.result.longitude}")
            }

    }

    fun mapMoveToPosition(map: Map, lat: String, lon: String) {
        map.move(
            CameraPosition(
                Point(lat.toDouble(), lon.toDouble()),
                6.0f,
                0.0f,
                0.0f
            ),
            LINEAR_ANIMATION
        ) {}

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

    fun saveToDataStore(name: String) {
        val dataStoreKey = stringPreferencesKey(LOCATION)
        runBlocking {
            application.dataStore.edit { settings ->
                settings[dataStoreKey] = name
            }
        }
    }

    fun changeDefiniteLocation(query: String) {
        viewModelScope.launch {
            _definiteLocation.value = requestSearchLocationUseCase(query)
        }
    }

    fun changeSavedLocation(query: String) {
        viewModelScope.launch {
            _savedLocation.value = requestSearchLocationUseCase(query)
        }
    }


    companion object {

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