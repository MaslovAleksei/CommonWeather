package com.margarin.commonweather.ui

sealed class WeatherEvent

class RefreshWeatherEvent(val name: String): WeatherEvent()