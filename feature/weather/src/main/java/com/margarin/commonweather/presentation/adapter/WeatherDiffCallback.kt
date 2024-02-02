package com.margarin.commonweather.presentation.adapter

import androidx.recyclerview.widget.DiffUtil

class WeatherDiffCallback : DiffUtil.ItemCallback<com.margarin.commonweather.weather.models.ByHoursWeatherModel>() {
    override fun areItemsTheSame(
        oldItem: com.margarin.commonweather.weather.models.ByHoursWeatherModel,
        newItem: com.margarin.commonweather.weather.models.ByHoursWeatherModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: com.margarin.commonweather.weather.models.ByHoursWeatherModel,
        newItem: com.margarin.commonweather.weather.models.ByHoursWeatherModel
    ): Boolean {
        return oldItem == newItem
    }
}