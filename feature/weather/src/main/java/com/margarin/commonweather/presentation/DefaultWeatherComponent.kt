package com.margarin.commonweather.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.margarin.commonweather.componentScope
import com.margarin.commonweather.search.City
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultWeatherComponent @AssistedInject constructor(
    private val storeFactory: WeatherStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onAddClicked") private val onAddClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : WeatherComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    WeatherStore.Label.ClickAdd -> {
                        onAddClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<WeatherStore.State> = store.stateFlow

    override fun onClickAdd() {
        store.accept(WeatherStore.Intent.ClickAdd)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("city") city: City,
            @Assisted("onAddClicked")  onAddClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultWeatherComponent
    }
}