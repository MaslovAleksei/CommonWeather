package com.margarin.commonweather.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.commonweather.domain.usecases.DeleteSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSearchLocationUseCase
import com.margarin.commonweather.domain.usecases.AddSearchItemUseCase
import com.margarin.commonweather.domain.usecases.GetSearchListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getSearchLocationUseCase: GetSearchLocationUseCase,
    private val getSearchItemUseCase: GetSearchItemUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val deleteSearchItemUseCase: DeleteSearchItemUseCase,
    private val getSearchListUseCase: GetSearchListUseCase,
    private val mainViewModel: MainViewModel
) : ViewModel() {

    private val _searchLocation =
        MutableLiveData<List<SearchModel>?>()
    val searchLocation: LiveData<List<SearchModel>?>
        get() = _searchLocation

    private var _searchList: LiveData<List<SearchModel>>? =
        null
    val searchList: LiveData<List<SearchModel>>?
        get() = _searchList

    private val _searchItem =
        MutableLiveData<SearchModel?>()
    val searchItem: LiveData<SearchModel?>
        get() = _searchItem

    fun getSearchLocation(query: String) {
        viewModelScope.launch {
            _searchLocation.value = getSearchLocationUseCase(query)
        }
    }

    fun loadSearchList() {
        viewModelScope.launch {
            _searchList = getSearchListUseCase()
        }
    }

    fun addSearchItem(searchModel: SearchModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addSearchItemUseCase(searchModel)
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

    fun changeSearchItem(city: String){
        mainViewModel.loadDataFromApi(location = city)
    }

    fun changeIsMenuShown(searchModel: SearchModel) {
        viewModelScope.launch {
            val newItem = searchModel.copy(isMenuShown = !searchModel.isMenuShown)
            addSearchItemUseCase(newItem)
        }
    }
}