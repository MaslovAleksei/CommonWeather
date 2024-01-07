package com.margarin.commonweather.ui

sealed class WeatherEvent

class LoadWeatherEvent(val name: String, val lang: String): WeatherEvent()