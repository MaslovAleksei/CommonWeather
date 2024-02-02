package com.margarin.commonweather.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.margarin.commonweather.domain.City

class SearchDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}