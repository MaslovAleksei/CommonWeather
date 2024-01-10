package com.margarin.commonweather.presentation.screens.search

import com.margarin.commonweather.domain.SearchModel

sealed class SearchState {

    data object StopQueryText : SearchState()
    class OnQueryText(val queryList: List<SearchModel>?) : SearchState()
}
