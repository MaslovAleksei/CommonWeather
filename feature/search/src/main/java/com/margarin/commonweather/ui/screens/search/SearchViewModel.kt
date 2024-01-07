package com.margarin.commonweather.ui.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.SearchModel
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.RequestSearchLocationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val requestSearchLocationUseCase: RequestSearchLocationUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: MutableLiveData<SearchState>
        get() = _state

    fun send(event: SearchEvent) {
        when (event) {
            is AddSearchItem -> {
                addSearchItem(event.searchModel)
            }
            is OnQueryTextLocation -> {
                onQueryTextLocation(event.query)
            }
            is StopQuery -> {
                stopQuery()
            }
        }
    }

    private fun addSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addSearchItemUseCase(searchModel)
        }
    }

    private fun onQueryTextLocation(query: String) {
        viewModelScope.launch {
            _state.value = OnQueryText(requestSearchLocationUseCase(query))
        }
    }

    private fun stopQuery() {
        _state.value = StopQueryText
    }
}