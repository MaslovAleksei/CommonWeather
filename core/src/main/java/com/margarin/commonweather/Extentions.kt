package com.margarin.commonweather

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.runBlocking

fun saveToDataStore(context: Context, stringPreferencesKey: String, value: String) {
    val dataStoreKey = stringPreferencesKey(stringPreferencesKey)
    runBlocking {
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
}
