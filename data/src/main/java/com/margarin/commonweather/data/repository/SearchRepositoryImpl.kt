package com.margarin.commonweather.data.repository

import androidx.lifecycle.map
import com.margarin.commonweather.data.database.dao.SearchDao
import com.margarin.commonweather.data.mapper.SearchMapper
import com.margarin.commonweather.data.api.ApiService
import com.margarin.commonweather.domain.SearchRepository
import com.margarin.commonweather.domain.models.SearchModel
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val searchMapper: SearchMapper,
    private val searchDao: SearchDao
) : SearchRepository {

    override suspend fun requestSearchLocation(query: String): List<SearchModel>? {
        var result: List<SearchModel>? = mutableListOf()
        try {
            result = apiService.getSearchWeather(query = query)?.map {
                searchMapper.mapSearchDtoToSearchModel(it)
            }
        } catch (_: Exception) { }
        return result
    }

    override suspend fun addSearchItem(searchModel: SearchModel) {
        val searchDbModel = searchMapper.mapSearchModelToSearchDbModel(searchModel)
        searchDao.addSearchItem(searchDbModel)
    }

    override fun getSearchList() = searchDao.getSearchList().map {
        it.map { searchDbModel ->
            searchMapper.mapSearchDbModelToSearchModel(searchDbModel)
        }
    }

    override suspend fun deleteSearchItem(searchModel: SearchModel) {
        searchDao.deleteSearchItem(searchModel.id)
    }
}