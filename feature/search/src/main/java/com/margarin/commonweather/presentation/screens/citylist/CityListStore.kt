package com.margarin.commonweather.presentation.screens.citylist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.margarin.commonweather.presentation.screens.citylist.CityListStore.Intent
import com.margarin.commonweather.presentation.screens.citylist.CityListStore.Label
import com.margarin.commonweather.presentation.screens.citylist.CityListStore.State
import com.margarin.commonweather.search.City
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CityListStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent
        data object ClickSearch : Intent
        data object DeleteFromFavourite : Intent
        data class CityItemClick(val city: City) : Intent
    }

    data class State(
        val cityItem: List<CityItem>
    ) {
        data class CityItem(

            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {

            data object Initial : WeatherState
            data object Loading : WeatherState
            data object Error : WeatherState
            data class Loaded(
                val tempC: String,
                val tempMinMax: String
            ) : WeatherState
        }
    }

    sealed interface Label {

        data object ClickBack : Label
        data object ClickSearch : Label
        data object DeleteFromFavourite : Label
        data class CityItemClick(val city: City) : Label
    }
}

class CityListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: com.margarin.commonweather.search.usecases.GetFavouriteCitiesUseCase,
    private val getWeatherUseCase: com.margarin.commonweather.weather.usecases.GetWeatherUseCase
) {

    fun create(): CityListStore =
        object : CityListStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CityListStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class FavouriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {

        data class WeatherLoadingError(val cityId: Int) : Msg
        data class WeatherIsLoading(val cityId: Int) : Msg
        data class FavouriteCitiesLoaded(val cities: List<City>) : Msg
        data class WeatherLoaded(
            val cityId: Int,
            val tempC: String,
            val tempMinMax: String
        ) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect {
                    dispatch(Action.FavouriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CityItemClick -> {
                    publish(Label.CityItemClick(intent.city))
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }

                Intent.DeleteFromFavourite -> {
                    publish(Label.DeleteFromFavourite)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {

            when (action) {
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                getWeatherUseCase(city.name.toString()).collect {
                    dispatch(
                        Msg.WeatherLoaded(
                            cityId = city.id,
                            tempC = it.currentWeatherModel?.temp_c.toString(),
                            tempMinMax = it.byDaysWeatherModel?.get(0)?.maxtemp_c.toString()
                        )
                    )
                }
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavouriteCitiesLoaded -> {
                copy(
                    cityItem = msg.cities.map {
                        State.CityItem(city = it, weatherState = State.WeatherState.Initial)
                    }
                )
            }

            is Msg.WeatherIsLoading -> {
                copy(
                    cityItem = cityItem.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = State.WeatherState.Loading)
                        } else {
                            it
                        }
                    }
                )
            }

            is Msg.WeatherLoaded -> {
                copy(
                    cityItem = cityItem.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Loaded(
                                    tempC = msg.tempC,
                                    tempMinMax = msg.tempMinMax
                                )
                            )
                        } else {
                            it
                        }
                    }
                )
            }

            is Msg.WeatherLoadingError -> {
                copy(
                    cityItem = cityItem.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Error
                            )
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }
}
