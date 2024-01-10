package com.margarin.commonweather.di

import com.margarin.commonweather.presentation.screens.citylist.CityListFragment
import com.margarin.commonweather.Feature
import com.margarin.commonweather.presentation.screens.search.SearchFragment

@Feature
interface SearchComponent {

    fun injectSearchFragment(searchFragment: SearchFragment)
    fun injectCityListFragment(cityListFragment: CityListFragment)

}
