package com.margarin.commonweather.presentation

sealed class WeatherEvent {

    class RefreshWeather(val name: String) : WeatherEvent()
}