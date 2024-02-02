package com.margarin.commonweather.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.margarin.commonweather.presentation.screens.search.compose.ui.theme.CommonWeatherTheme

@Composable
fun RootContent(component: DefaultRootComponent) {

    CommonWeatherTheme {
        Children(stack = component.stack) {
            when(val instance = it.instance) {
                is RootComponent.Child.CityList -> {

                }

                is RootComponent.Child.Search -> {

                }

                is RootComponent.Child.Weather -> {

                }
            }
        }
    }
}