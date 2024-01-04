package com.margarin.commonweather.ui.searchscreen.adapter

import androidx.recyclerview.widget.DiffUtil
import com.margarin.commonweather.models.SearchModel

class SearchDiffCallback : DiffUtil.ItemCallback<SearchModel>() {
    override fun areItemsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
        return oldItem == newItem
    }
}