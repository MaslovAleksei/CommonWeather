package com.margarin.commonweather.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.commonweather.domain.usecases.DeleteSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSearchLocationUseCase
import com.margarin.commonweather.domain.usecases.InsertSearchItemUseCase
import com.margarin.commonweather.domain.usecases.LoadSearchListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getSearchLocationUseCase: GetSearchLocationUseCase,
    private val getSearchItemUseCase: GetSearchItemUseCase,
    private val insertSearchItemUseCase: InsertSearchItemUseCase,
    private val deleteSearchItemUseCase: DeleteSearchItemUseCase,
    private val loadSearchListUseCase: LoadSearchListUseCase
): ViewModel() {

    private val _searchLocation = MutableLiveData<List<SearchModel>?>()
    val searchLocation: LiveData<List<SearchModel>?>
        get() = _searchLocation

    private var _searchList: LiveData<List<SearchModel>>? = null
    val searchList: LiveData<List<SearchModel>>?
        get() = _searchList

    private val _searchItem = MutableLiveData<SearchModel?>()
    val searchItem: LiveData<SearchModel?>
        get() = _searchItem

    fun getSearchLocation(query: String) {
        viewModelScope.launch {
            _searchLocation.value = getSearchLocationUseCase(query)
        }
    }

    fun loadSearchList() {
        viewModelScope.launch {
            _searchList = loadSearchListUseCase()
        }
    }

    fun insertSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO){
            insertSearchItemUseCase(searchModel)
        }
    }

    fun deleteSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSearchItemUseCase(searchModel)
        }
    }

    fun getSearchItem(searchId: Int) {
        viewModelScope.launch {
            _searchItem.value = getSearchItemUseCase(searchId)
        }
    }
}