package com.margarin.commonweather.presentation.screens.citylist

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

class DefaultCityListComponent @AssistedInject constructor(
    private val storeFactory: CityListStoreFactory,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("onSearchClick") private val onSearchClick: () -> Unit,
    @Assisted("onDeleteSwipe") private val onDeleteSwipe: () -> Unit,
    @Assisted("onCityItemClick") private val onCityItemClick: (City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : CityListComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is CityListStore.Label.CityItemClick -> {
                        onCityItemClick(it.city)
                    }

                    CityListStore.Label.ClickBack -> {
                        onBackClick()
                    }

                    CityListStore.Label.ClickSearch -> {
                        onSearchClick()
                    }

                    CityListStore.Label.DeleteFromFavourite -> {
                        onDeleteSwipe()
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CityListStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(CityListStore.Intent.ClickBack)
    }

    override fun onClickSearch() {
        store.accept(CityListStore.Intent.ClickSearch)
    }

    override fun onSwipeDelete() {
        store.accept(CityListStore.Intent.DeleteFromFavourite)
    }

    override fun onClickCityItem(city: City) {
        store.accept(CityListStore.Intent.CityItemClick(city))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("onSearchClick") onSearchClick: () -> Unit,
            @Assisted("onDeleteSwipe") onDeleteSwipe: () -> Unit,
            @Assisted("onCityItemClick") onCityItemClick: (City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultCityListComponent
    }
}