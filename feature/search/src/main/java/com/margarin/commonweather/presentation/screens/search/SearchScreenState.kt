package com.margarin.commonweather.presentation.screens.search

import com.margarin.commonweather.domain.SearchModel

sealed class SearchScreenState {

    class SearchesList(val queryList: List<SearchModel>?) : SearchScreenState()
}
