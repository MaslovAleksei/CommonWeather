package com.margarin.commonweather.di

import com.margarin.commonweather.ui.screens.CityListFragment
import com.margarin.commonweather.Feature
import com.margarin.commonweather.ui.screens.SearchFragment

@Feature
interface SearchComponent {

    fun injectSearchFragment(searchFragment: SearchFragment)
    fun injectCityListFragment(cityListFragment: CityListFragment)

}
