package com.margarin.commonweather.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.weather.R
import com.margarin.weather.databinding.HourItemBinding

class WeatherAdapter() : ListAdapter<ByHoursWeatherModel, WeatherHolder>(WeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {

        val binding =  HourItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return WeatherHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvTemp.text = item.temp_c.toString()
            tvTime.text = item.time
            tvWindSpeed.text = item.wind_kph.toString()
            ivCondition.setImageResource(item.icon_url ?: R.drawable.ic_loading)
        }
    }
}