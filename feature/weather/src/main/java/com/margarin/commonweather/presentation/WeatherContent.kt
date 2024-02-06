package com.margarin.commonweather.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(component: WeatherComponent) {

    val state by component.model.collectAsState()
    val weatherState = state.weatherState

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = state.city.name.toString())
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val weatherState = state.weatherState) {
                WeatherStore.State.WeatherState.Error -> {

                }

                WeatherStore.State.WeatherState.Initial -> {

                }

                is WeatherStore.State.WeatherState.Loaded -> {

                }

                WeatherStore.State.WeatherState.Loading -> {

                }

            }
        }
    }
}