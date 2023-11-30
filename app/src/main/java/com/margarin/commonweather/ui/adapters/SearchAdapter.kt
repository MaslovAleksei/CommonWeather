package com.margarin.commonweather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.margarin.commonweather.databinding.SearchItemBinding
import com.margarin.commonweather.domain.models.SearchModel

class SearchAdapter: ListAdapter<SearchModel, SearchHolder>(SearchDiffCallback())  {

    var onItemClickListener: ((SearchModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val binding = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvCity.text = item.name
            tvCountry.text = "${item.name}, ${item.country}"
            root.setOnClickListener{
                onItemClickListener?.invoke(item)
            }
        }
    }
}