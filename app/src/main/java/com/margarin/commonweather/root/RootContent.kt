package com.margarin.commonweather.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.margarin.commonweather.presentation.WeatherContent
import com.margarin.commonweather.presentation.screens.citylist.CityListContent
import com.margarin.commonweather.presentation.screens.search.SearchContent
import com.margarin.commonweather.ui.theme.CommonWeatherTheme

@Composable
fun RootContent(component: DefaultRootComponent) {

    CommonWeatherTheme {
        Children(stack = component.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.CityList -> {
                    CityListContent(component = instance.component)
                }

                is RootComponent.Child.Search -> {
                    SearchContent(component = instance.component)
                }

                is RootComponent.Child.Weather -> {
                    WeatherContent(component = instance.component)
                }
            }
        }
    }
}