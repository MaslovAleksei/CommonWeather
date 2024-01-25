package com.margarin.commonweather.presentation.screens.search

import com.margarin.commonweather.domain.SearchModel

sealed class SearchEvent {

    class AddSearchItem(val searchModel: SearchModel) : SearchEvent()
    class OnQuery(val query: String) : SearchEvent()
}

