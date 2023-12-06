package com.margarin.commonweather.utils

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import com.margarin.commonweather.R
import com.margarin.commonweather.ui.screens.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

fun Fragment.launchFragment(fragment: Fragment, fragmentName: String){
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.holder, fragment)
        .addToBackStack(fragmentName)
        .commit()
}

suspend fun Fragment.saveInDataStore(key: String, value: String) {
    val dataStoreKey = stringPreferencesKey(key)
    requireContext().dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

suspend fun Fragment.readFromDataStore(key: String): String? {
    val dataStoreKey = stringPreferencesKey(key)
    val preferences = runBlocking {(requireContext().dataStore.data.first())}
    return preferences[dataStoreKey]
}

