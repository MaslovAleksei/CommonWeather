package com.margarin.commonweather.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.margarin.commonweather.presentation.WeatherComponent
import com.margarin.commonweather.presentation.screens.citylist.CityListComponent
import com.margarin.commonweather.presentation.screens.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Weather(val component: WeatherComponent) : Child

        data class Search(val component: SearchComponent) : Child

        data class CityList(val component: CityListComponent) : Child
    }
}