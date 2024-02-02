package com.margarin.commonweather.presentation.screens.search

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.margarin.commonweather.PermissionManager
import com.margarin.commonweather.isGpsEnabled
import com.margarin.commonweather.makeToast
import com.margarin.search.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchCityUseCase: com.margarin.commonweather.search.usecases.SearchCityUseCase,
    private val addSearchItemUseCase: com.margarin.commonweather.search.usecases.AddToFavouriteUseCase,
    private val application: Application
) : ViewModel() {

    private val _state =
        MutableStateFlow<SearchScreenState>(SearchScreenState.Initial)
    val state = _state.asStateFlow()

    internal fun sendEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.AddSearchItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addSearchItemUseCase(event.city)
                }
            }

            is SearchEvent.OnQuery -> {
                viewModelScope.launch {
                    _state.value =
                        SearchScreenState.SearchesList(searchCityUseCase(event.query))
                }
            }

            is SearchEvent.UseGps -> {
                if (isGpsEnabled(application)) {
                    interactWithCurrentLocation(fusedLocationClient = event.fusedLocationClient)
                } else {
                    makeToast(application, application.getString(R.string.switch_on_gps))
                    application.startActivity(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
        }
    }

    private fun interactWithCurrentLocation(
        fusedLocationClient: FusedLocationProviderClient
    ) {
        _state.value = SearchScreenState.Locating
        if (PermissionManager.checkLocationPermission(application)) {
            makeToast(
                context = application,
                text = application.getString(R.string.navigation_permissions_not_allowed)
            )
            return
        }
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener {
                val latLon = "${it.result.latitude}, ${it.result.longitude}"
                requestSearchLocation(latLon)
            }
    }

    private fun requestSearchLocation(query: String) {
        viewModelScope.launch {
            searchCityUseCase(query).apply {
                if (this?.isNotEmpty() == true) {
                    addSearchItemUseCase(this.first())
                    _state.value = SearchScreenState.Close
                } else {
                    makeToast(application, application.getString(R.string.settlement_not_found))
                }
            }
        }

    }
}