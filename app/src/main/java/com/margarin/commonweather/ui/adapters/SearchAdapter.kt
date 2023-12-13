package com.margarin.commonweather.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.margarin.commonweather.R
import com.margarin.commonweather.databinding.CityItemBinding
import com.margarin.commonweather.databinding.SearchItemBinding
import com.margarin.commonweather.domain.models.SearchModel

class SearchAdapter(private var layout: Int) :
    ListAdapter<SearchModel, SearchHolder>(SearchDiffCallback()) {

    var onItemClickListener: ((SearchModel) -> Unit)? = null
    var onButtonDeleteClickListener: ((SearchModel) -> Unit)? = null
    var onButtonAddToFavClickListener: ((SearchModel) -> Unit)? = null
    var onSwipeItemClickListener: ((SearchModel) -> Unit)? = null


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
                holder.binding.tvCity.text = item.name
                val countryName = "${item.name}, ${item.country}"
                holder.binding.tvCountry.text = countryName
                holder.binding.root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
                holder.binding.bAddToFav.setOnClickListener {
                    onButtonAddToFavClickListener?.invoke(item)
                }
            }

            is CityItemBinding -> {
                holder.binding.tvName.text = item.name
                holder.binding.root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
                holder.binding.bDelete.setOnClickListener {
                    onButtonDeleteClickListener?.invoke(item)
                }
                if (item.isMenuShown) {
                    holder.binding.bDelete.visibility = View.VISIBLE
                }


            }
        }
    }


}