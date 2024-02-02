package com.margarin.commonweather.presentation.screens.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.margarin.commonweather.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSearchComponent @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("onOpenForecastClick") private val onOpenForecastClick: (com.margarin.commonweather.search.City) -> Unit,
    @Assisted("onSaveToFavouriteClick") private val onSaveToFavouriteClick: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect{
                when(it) {
                    SearchStore.Label.ClickBack -> {
                        onBackClick()
                    }

                    is SearchStore.Label.OpenWeather -> {
                        onOpenForecastClick(it.city)
                    }

                    is SearchStore.Label.SavedToFavourite -> {
                        onSaveToFavouriteClick()
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onClickDefineLocation() {
        store.accept(SearchStore.Intent.DefineLocation)
    }

    override fun onClickCity(city: com.margarin.commonweather.search.City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    override fun onClickAddToFavourite(city: com.margarin.commonweather.search.City) {
        store.accept(SearchStore.Intent.ClickAddFavourite(city))
    }

    override fun onStopSearch() {
        store.accept(SearchStore.Intent.StopSearch)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("onOpenForecastClick") onOpenForecastClick: (com.margarin.commonweather.search.City) -> Unit,
            @Assisted("onSaveToFavouriteClick") onSaveToFavouriteClick: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ):DefaultSearchComponent
    }
}