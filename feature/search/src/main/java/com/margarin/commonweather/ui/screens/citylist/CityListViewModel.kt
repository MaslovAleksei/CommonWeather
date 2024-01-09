package com.margarin.commonweather.ui.screens.citylist

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.margarin.commonweather.PermissionManager
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.DeleteSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSavedCityListUseCase
import com.margarin.commonweather.domain.usecases.RequestSearchLocationUseCase
import com.margarin.commonweather.isGpsEnabled
import com.margarin.commonweather.makeToast
import com.margarin.commonweather.utils.YandexMapManager
import com.margarin.search.R
import com.yandex.mapkit.map.Map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityListViewModel @Inject constructor(
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val deleteSearchItemUseCase: DeleteSearchItemUseCase,
    private val getSavedCityListUseCase: GetSavedCityListUseCase,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow<CityListState>(CityListState.EmptyList)
    val state = _state.asStateFlow()

    fun send(event: CityListEvent) {
        when (event) {
            is CityListEvent.GetSavedCityList -> {
                viewModelScope.launch {
                    getSavedCityListUseCase()
                        .onEach { _state.value = CityListState.Content(it) }
                        .filter { it.isEmpty() }
                        .collect {
                            _state.value = CityListState.EmptyList
                        }
                }
            }

            is CityListEvent.AddSearchItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addSearchItemUseCase(event.searchModel)
                }
            }

            is CityListEvent.DeleteSearchItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteSearchItemUseCase(event.searchModel)
                }
            }

            is CityListEvent.RequestSearchLocation -> {
                requestSearchLocation(event.query)
            }

            is CityListEvent.UseGps -> {
                if (isGpsEnabled(application)) {
                    interactWithCurrentLocation(
                        fusedLocationClient = event.fusedLocationClient,
                        isMapGone = event.isMapGone,
                        yandexMapManager = event.yandexMapManager,
                        map = event.map
                    )
                } else {
                    makeToast(application, application.getString(R.string.switch_on_gps))
                    application.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }

            is CityListEvent.OpenMap -> {
                _state.value = CityListState.OpenedMap
            }
        }
    }

    private fun interactWithCurrentLocation(
        fusedLocationClient: FusedLocationProviderClient,
        isMapGone: Boolean,
        yandexMapManager: YandexMapManager,
        map: Map
    ) {
        _state.value = CityListState.Locating
        if (PermissionManager.checkLocationPermission(application)) {
            makeToast(
                application,
                application.getString(R.string.navigation_permissions_not_allowed)
            )
            return
        }
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener {
                if (isMapGone) {
                    val latLon = "${it.result.latitude}, ${it.result.longitude}"
                    requestSearchLocation(latLon)
                } else {
                    yandexMapManager.mapMoveToPosition(
                        map,
                        it.result.latitude.toString(),
                        it.result.longitude.toString()
                    )
                    _state.value = CityListState.OpenedMap
                }
            }
    }

    private fun requestSearchLocation(query: String) {
        viewModelScope.launch {
            val requestedLocation = requestSearchLocationUseCase(query)
            if (requestedLocation?.isNotEmpty() == true) {
                addSearchItemUseCase(requestedLocation.first())
            } else {
                makeToast(application, application.getString(R.string.settlement_not_found))
            }
        }
    }
}