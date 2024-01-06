package com.margarin.commonweather.ui.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.margarin.commonweather.domain.SearchModel
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.DeleteSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSavedCityListUseCase
import com.margarin.commonweather.domain.usecases.RequestSearchLocationUseCase
import com.margarin.search.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityListViewModel @Inject constructor(
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val deleteSearchItemUseCase: DeleteSearchItemUseCase,
    private val getSavedCityListUseCase: GetSavedCityListUseCase,
    private val application: Application
) : ViewModel() {

    private val _definiteLocation = MutableLiveData<List<SearchModel>?>()
    val definiteLocation: LiveData<List<SearchModel>?>
        get() = _definiteLocation

    private var _savedCityList: LiveData<List<SearchModel>>? = null
    val savedCityList: LiveData<List<SearchModel>>?
        get() = _savedCityList

    fun getSearchList() {
        viewModelScope.launch {
            _savedCityList = getSavedCityListUseCase()
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
        ) {
        }
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener {
                viewModelScope.launch {
                    _definiteLocation.value = requestSearchLocationUseCase(
                            "${it.result.latitude}, ${it.result.longitude}"
                        )
                }
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
                getString(
                    application,
                    R.string.point_the_center_of_the_map_at_the_location_and_press_save_point
                ),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun attemptSaveLocation(query: String) {
        viewModelScope.launch {
            val requestedLocation = requestSearchLocationUseCase(query)
            if (requestedLocation?.isNotEmpty() == true) {
                addSearchItem(requestedLocation.first())
            } else {
                Toast.makeText(
                    application,
                    R.string.settlement_not_found, Toast.LENGTH_SHORT
                ).show()
            }
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