package com.margarin.commonweather.ui.mainscreen.adapter

import androidx.recyclerview.widget.DiffUtil
import com.margarin.commonweather.models.ByHoursWeatherModel

class WeatherDiffCallback : DiffUtil.ItemCallback<ByHoursWeatherModel>() {
    override fun areItemsTheSame(
        oldItem: ByHoursWeatherModel,
        newItem: ByHoursWeatherModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ByHoursWeatherModel,
        newItem: ByHoursWeatherModel
    ): Boolean {
        return oldItem == newItem
    }
}