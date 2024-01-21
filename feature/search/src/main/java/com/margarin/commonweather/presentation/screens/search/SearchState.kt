package com.margarin.commonweather.presentation.screens.search

import com.margarin.commonweather.domain.SearchModel

sealed class SearchState {

    data object Initial : SearchState()
    data object StoppedQueryText : SearchState()
    class StartedQueryText(val queryList: List<SearchModel>?) : SearchState()
}
