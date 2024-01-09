package com.margarin.commonweather

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

suspend fun saveToDataStore(context: Context, stringPreferencesKey: String, value: String) {
    val dataStoreKey = stringPreferencesKey(stringPreferencesKey)
    context.dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

suspend fun loadFromDataStore(
    context: Context,
    stringPreferencesKey: String,
    defaultCity: String
): String {
    val preferences = (context.dataStore.data.first())
    return preferences[stringPreferencesKey(stringPreferencesKey)] ?: defaultCity
}

fun makeToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun isGpsEnabled(context: Context): Boolean {
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
    return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
}

