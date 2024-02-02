package com.margarin.commonweather.presentation

import kotlinx.coroutines.flow.StateFlow

interface WeatherComponent {

    val model: StateFlow<WeatherStore.State>

    fun onClickAdd()


}