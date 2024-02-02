package com.margarin.commonweather.presentation.screens.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.margarin.commonweather.presentation.screens.search.SearchStore.Intent
import com.margarin.commonweather.presentation.screens.search.SearchStore.Label
import com.margarin.commonweather.presentation.screens.search.SearchStore.State
import com.margarin.commonweather.search.City
import com.margarin.commonweather.search.usecases.AddToFavouriteUseCase
import com.margarin.commonweather.search.usecases.SearchCityUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object StopSearch : Intent
        data object ClickBack : Intent
        data object DefineLocation : Intent
        data class ClickCity(val city: City) : Intent
        data class ClickAddFavourite(val city: City) : Intent
        data class ChangeSearchQuery(val query: String) : Intent
    }

    data class State(

        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {

            data object Initial : SearchState
            data object Loading : SearchState
            data object Error : SearchState
            data object EmptyResult : SearchState
            data object StopSearch : SearchState
            data object Define : SearchState
            data class SuccessLoaded(val cities: List<City>) : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
        data class SavedToFavourite(val city: City) : Label
        data class OpenWeather(val city: City) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase
) {

    fun create(): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeSearchQuery(val query: String) : Msg
        data class SearchResultLoaded(val cities: List<City>) : Msg
        data object StopSearch : Msg
        data object LoadingSearchResult : Msg
        data object SearchResultError : Msg
        data object DefineLocation : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {

            when (intent) {
                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(Msg.LoadingSearchResult)
                        try {
                            val cities = searchCityUseCase(getState().searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.SearchResultError)
                        }
                    }


                }

                is Intent.ClickAddFavourite -> {
                    scope.launch {
                        addToFavouriteUseCase(intent.city)
                        publish(Label.SavedToFavourite(intent.city))
                    }
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickCity -> {
                    publish(Label.OpenWeather(intent.city))
                }

                Intent.DefineLocation -> {
                    dispatch(Msg.DefineLocation)
                }

                Intent.StopSearch -> {
                    dispatch(Msg.StopSearch)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeSearchQuery -> {
                copy(searchQuery = msg.query)
            }

            Msg.DefineLocation -> {
                copy(searchState = State.SearchState.Define)
            }

            Msg.LoadingSearchResult -> {
                copy(searchState = State.SearchState.Loading)
            }

            Msg.SearchResultError -> {
                copy(searchState = State.SearchState.Error)
            }

            is Msg.SearchResultLoaded -> {
                val searchState = if (msg.cities.isEmpty()) {
                    State.SearchState.EmptyResult
                } else {
                    State.SearchState.SuccessLoaded(msg.cities)
                }
                copy(searchState = searchState)
            }

            Msg.StopSearch -> {
                copy(searchState = State.SearchState.StopSearch)
            }
        }
    }
}
