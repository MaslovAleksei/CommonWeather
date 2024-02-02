package com.margarin.commonweather.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.ListAdapter
import com.margarin.commonweather.search.City
import com.margarin.search.R
import com.margarin.search.databinding.CityItemBinding
import com.margarin.search.databinding.SearchItemBinding

class SearchAdapter(private var layout: Int) :
    ListAdapter<City, SearchHolder>(SearchDiffCallback()) {

    var onItemClickListener: ((City) -> Unit)? = null
    var onButtonDeleteClickListener: ((City) -> Unit)? = null
    var onButtonAddToFavClickListener: ((City) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {

        val binding = when (layout) {
            R.layout.search_item -> SearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            R.layout.city_item -> CityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else -> throw RuntimeException("Unknown layout: $layout")
        }
        return SearchHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val item = getItem(position)
        when (holder.binding) {
            is SearchItemBinding -> {
                holder.binding.apply {
                    tvCity.text = item.name
                    val countryName = "${item.name}, ${item.country}"
                    tvCountry.text = countryName
                    root.setOnClickListener { onItemClickListener?.invoke(item) }
                    bAddToFav.setOnClickListener { onButtonAddToFavClickListener?.invoke(item) }
                }
            }
            is CityItemBinding -> {
                holder.binding.apply {
                    tvName.text = item.name
                    root.setOnClickListener { onItemClickListener?.invoke(item) }
                    root.setOnLongClickListener {
                        if (bDelete.isGone) {
                            bDelete.visibility = View.VISIBLE
                        } else {
                            bDelete.visibility = View.GONE
                        }
                        true
                    }
                    bDelete.setOnClickListener {
                        onButtonDeleteClickListener?.invoke(item)
                        bDelete.visibility = View.GONE
                    }
                }
            }
        }
    }
}