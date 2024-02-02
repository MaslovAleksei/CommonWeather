package com.margarin.commonweather.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable

import com.margarin.commonweather.presentation.DefaultWeatherComponent
import com.margarin.commonweather.presentation.screens.citylist.DefaultCityListComponent
import com.margarin.commonweather.presentation.screens.search.DefaultSearchComponent
import com.margarin.commonweather.search.City
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val weatherComponentFactory: DefaultWeatherComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    private val cityListComponentFactory: DefaultCityListComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Weather(
            City(
                id = DEFAULT_LOCATION_ID,
                name = null,
                region = null,
                country = null,
                lat = null,
                lon = null,
                url = null
            )
        ),
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            Config.CityList -> {
                val component = cityListComponentFactory.create(
                    onBackClick = { navigation.pop() },
                    onSearchClick = { navigation.push(Config.Search)},
                    onDeleteSwipe = {  },
                    onCityItemClick = { navigation.push(Config.Weather(city = it)) },
                    componentContext = componentContext
                )
                RootComponent.Child.CityList(component)
            }

            Config.Search -> {
                val component = searchComponentFactory.create(
                    onBackClick = { navigation.pop() },
                    onOpenForecastClick = { navigation.push(Config.Weather(it)) },
                    onSaveToFavouriteClick = {  },
                    componentContext = componentContext
                )
                RootComponent.Child.Search(component)
            }

            is Config.Weather -> {
                val component = weatherComponentFactory.create(
                    city = config.city,
                    onAddClicked = { navigation.push(Config.CityList) },
                    componentContext = componentContext
                )
                RootComponent.Child.Weather(component)
            }

        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object CityList : Config

        @Parcelize
        data object Search : Config

        @Parcelize
        data class Weather(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }

    companion object {
        private const val DEFAULT_LOCATION_ID = 2145091
    }
}