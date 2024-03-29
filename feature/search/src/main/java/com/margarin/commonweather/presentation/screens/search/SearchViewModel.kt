package com.margarin.commonweather.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.RequestSearchLocationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state = _state.asStateFlow()

    internal fun sendEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.AddSearchItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addSearchItemUseCase(event.searchModel)
                }
            }
            is SearchEvent.StartQuery -> {
                viewModelScope.launch {
                    _state.value =
                        SearchState.StartedQueryText(requestSearchLocationUseCase(event.query))
                }
            }
            is SearchEvent.StopQuery -> {
                _state.value = SearchState.StoppedQueryText
            }
        }
    }
}