package com.margarin.commonweather.data

import androidx.lifecycle.map
import com.margarin.commonweather.ApiService
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val searchMapper: SearchMapper,
    private val searchDao: com.margarin.commonweather.dao.SearchDao
) : com.margarin.commonweather.domain.SearchRepository {

    override suspend fun requestSearchLocation(query: String): List<com.margarin.commonweather.domain.SearchModel>? {
        var result: List<com.margarin.commonweather.domain.SearchModel>? = mutableListOf()
        try {
            result = apiService.getSearchWeather(query = query)?.map {
                searchMapper.mapSearchDtoToSearchModel(it)
            }
        } catch (_: Exception) { }
        return result
    }

    override suspend fun addSearchItem(searchModel: com.margarin.commonweather.domain.SearchModel) {
        val searchDbModel = searchMapper.mapSearchModelToSearchDbModel(searchModel)
        searchDao.addSearchItem(searchDbModel)
    }

    override fun getSearchList() = searchDao.getSearchList().map {
        it.map { searchDbModel ->
            searchMapper.mapSearchDbModelToSearchModel(searchDbModel)
        }
    }

    override suspend fun deleteSearchItem(searchModel: com.margarin.commonweather.domain.SearchModel) {
        searchDao.deleteSearchItem(searchModel.id)
    }
}