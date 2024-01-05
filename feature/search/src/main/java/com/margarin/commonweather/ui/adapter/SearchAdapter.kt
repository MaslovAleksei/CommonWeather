package com.margarin.commonweather.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.ListAdapter
import com.margarin.commonweather.models.SearchModel
import com.margarin.search.R
import com.margarin.search.databinding.CityItemBinding
import com.margarin.search.databinding.SearchItemBinding

class SearchAdapter(private var layout: Int) :
    ListAdapter<SearchModel, SearchHolder>(SearchDiffCallback()) {

    var onItemClickListener: ((SearchModel) -> Unit)? = null
    var onButtonDeleteClickListener: ((SearchModel) -> Unit)? = null
    var onButtonAddToFavClickListener: ((SearchModel) -> Unit)? = null


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
                holder.binding.root.setOnLongClickListener {
                    if (holder.binding.bDelete.isGone) {
                        holder.binding.bDelete.visibility = View.VISIBLE
                    } else {
                        holder.binding.bDelete.visibility = View.GONE
                    }
                    true
                }
                holder.binding.bDelete.setOnClickListener {
                    onButtonDeleteClickListener?.invoke(item)
                    holder.binding.bDelete.visibility = View.GONE
                }


            }
        }
    }
}