package com.margarin.commonweather.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.margarin.commonweather.presentation.WeatherStore.Intent
import com.margarin.commonweather.presentation.WeatherStore.Label
import com.margarin.commonweather.presentation.WeatherStore.State
import com.margarin.commonweather.search.City
import com.margarin.commonweather.weather.models.WeatherModel
import com.margarin.commonweather.weather.usecases.GetWeatherUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

interface WeatherStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickAdd : Intent
    }

    data class State(
        val city: City,
        val weatherState: WeatherState
    ) {
        sealed interface WeatherState {

            data object Initial : WeatherState
            data object Loading : WeatherState
            data object Error : WeatherState
            data class Loaded(val weather: WeatherModel) : WeatherState
        }
    }

    sealed interface Label {
        data object ClickAdd : Label
    }
}

class WeatherStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWeatherUseCase: GetWeatherUseCase
) {

    fun create(city: City): WeatherStore =
        object : WeatherStore, Store<Intent, State, Label> by storeFactory.create(
            name = "WeatherStore",
            initialState = State(
                city = city,
                weatherState = State.WeatherState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class WeatherLoaded(val weather: WeatherModel) : Action
        data object WeatherStartLoading : Action
        data object WeatherLoadingError : Action
    }

    private sealed interface Msg {
        data class WeatherLoaded(val weather: WeatherModel) : Msg
        data object WeatherStartLoading : Msg
        data object WeatherLoadingError : Msg
    }

    private inner class BootstrapperImpl(
        private val city: City
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.WeatherStartLoading)
                try {
                    getWeatherUseCase(city.name.toString()).collect {
                        dispatch(Action.WeatherLoaded(weather = it))
                    }
                } catch (e: Exception) {
                    dispatch(Action.WeatherLoadingError)
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickAdd -> {
                    publish(Label.ClickAdd)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.WeatherLoaded -> {
                    dispatch(Msg.WeatherLoaded(action.weather))
                }

                Action.WeatherLoadingError -> {
                    dispatch(Msg.WeatherLoadingError)
                }

                Action.WeatherStartLoading -> {
                    dispatch(Msg.WeatherStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.WeatherLoaded -> {
                copy(weatherState = State.WeatherState.Loaded(msg.weather))
            }

            Msg.WeatherLoadingError -> {
                copy(weatherState = State.WeatherState.Error)
            }

            Msg.WeatherStartLoading -> {
                copy(weatherState = State.WeatherState.Loading)
            }
        }
    }
}
