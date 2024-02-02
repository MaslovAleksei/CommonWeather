package com.margarin.commonweather.presentation.adapter

import androidx.recyclerview.widget.DiffUtil

class SearchDiffCallback : DiffUtil.ItemCallback<com.margarin.commonweather.search.City>() {
    override fun areItemsTheSame(oldItem: com.margarin.commonweather.search.City, newItem: com.margarin.commonweather.search.City): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: com.margarin.commonweather.search.City, newItem: com.margarin.commonweather.search.City): Boolean {
        return oldItem == newItem
    }
}