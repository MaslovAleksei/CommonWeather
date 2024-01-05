package com.margarin.commonweather.di

import com.margarin.commonweather.CityListFragment
import com.margarin.commonweather.Feature
import com.margarin.commonweather.SearchFragment

@Feature
interface SearchComponent {

    fun injectSearchFragment(searchFragment: SearchFragment)
    fun injectCityListFragment(cityListFragment: CityListFragment)

}
