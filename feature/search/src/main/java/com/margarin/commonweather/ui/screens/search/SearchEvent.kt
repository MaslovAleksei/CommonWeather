package com.margarin.commonweather.ui.screens.search

import com.margarin.commonweather.domain.SearchModel

sealed class SearchEvent {

    class AddSearchItem(val searchModel: SearchModel) : SearchEvent()
    class OnQueryTextLocation(val query: String) : SearchEvent()
    data object StopQuery : SearchEvent()
}

