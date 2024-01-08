package com.margarin.commonweather.ui.screens.citylist

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.margarin.commonweather.PermissionManager
import com.margarin.commonweather.domain.SearchModel
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
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityListViewModel @Inject constructor(
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val deleteSearchItemUseCase: DeleteSearchItemUseCase,
    private val getSavedCityListUseCase: GetSavedCityListUseCase,
    private val application: Application
) : ViewModel() {

    private val _state = MutableLiveData<CityListState>()
    val state: LiveData<CityListState>
        get() = _state

    private var cityListLD: LiveData<List<SearchModel>>? = null

    fun send(event: CityListEvent) {

        when (event) {
            is GetSavedCityList -> {
                getSavedCityList()
            }
            is AddSearchItem -> {
                addSearchItem(searchModel = event.searchModel)
            }
            is DeleteSearchItem -> {
                deleteSearchItem(searchModel = event.searchModel)
            }
            is RequestSearchLocation -> {
                requestSearchLocation(query = event.query)
            }
            is UseGps -> {
                useGps(
                    fusedLocationClient = event.fusedLocationClient,
                    isMapGone = event.isMapGone,
                    yandexMapManager = event.yandexMapManager,
                    map = event.map
                )
            }
            is OpenMap -> {
                openMap()
            }
        }
    }

     private fun getSavedCityList() {
        viewModelScope.launch {
            cityListLD = getSavedCityListUseCase()

            /*
            if (cityListLD?.value?.get(0) == null) {
                _state.value = EmptyList
                return@launch
            }
             */

            cityListLD?.observeForever{
                _state.value = CityList(it)
            }
        }
    }

    private fun addSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addSearchItemUseCase(searchModel)
        }
    }

    private fun deleteSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSearchItemUseCase(searchModel)
        }
    }

    private fun requestSearchLocation(query: String) {
        viewModelScope.launch {
            val requestedLocation = requestSearchLocationUseCase(query)
            if (requestedLocation?.isNotEmpty() == true) {
                addSearchItem(requestedLocation.first())
            } else {
                makeToast(application, application.getString(R.string.settlement_not_found))
            }
        }
    }

    private fun openMap() {
        _state.value = OpenedMap
    }

    private fun useGps(fusedLocationClient: FusedLocationProviderClient,
               isMapGone: Boolean,
               yandexMapManager: YandexMapManager,
               map: Map) {
        if (isGpsEnabled(application)) {
                interactWithCurrentLocation(
                    fusedLocationClient = fusedLocationClient,
                    isMapGone = isMapGone,
                    yandexMapManager = yandexMapManager,
                    map = map
                )

        } else {
            makeToast(application, application.getString(R.string.switch_on_gps))
            application.startActivity(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    private fun interactWithCurrentLocation(
        fusedLocationClient: FusedLocationProviderClient,
        isMapGone: Boolean,
        yandexMapManager: YandexMapManager,
        map: Map
    ) {
        _state.value = Locating
        if (PermissionManager.checkLocationPermission(application)) {
            makeToast(application, application.getString(R.string.navigation_permissions_not_allowed))
            return
        }
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener {
                if (isMapGone) {
                    val latLon = "${it.result.latitude}, ${it.result.longitude}"
                    requestSearchLocation(latLon)
                    getSavedCityList()
                } else {
                    yandexMapManager.mapMoveToPosition(
                        map,
                        it.result.latitude.toString(),
                        it.result.longitude.toString()
                    )
                    _state.value = OpenedMap
                }
            }
    }

    override fun onCleared() {
        //list?.removeObservers()
        super.onCleared()
    }
}