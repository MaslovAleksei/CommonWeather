package com.margarin.commonweather.presentation

sealed class WeatherEvent {

    class RefreshWeatherEvent(val name: String) : WeatherEvent()
}