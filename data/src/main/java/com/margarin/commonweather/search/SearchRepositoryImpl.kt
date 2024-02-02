package com.margarin.commonweather.search

import com.margarin.commonweather.local.dao.SearchDao
import com.margarin.commonweather.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val searchMapper: SearchMapper,
    private val searchDao: SearchDao
) : SearchRepository {

    override suspend fun search(query: String): List<City> {
        var result: List<City> = mutableListOf()
        try {
            result = apiService.getCityWeather(query = query).map {
                searchMapper.mapCityDtoToCity(it)
            }
        } catch (_: Exception) {}
        return result
    }

    override val favouriteCities: Flow<List<City>> = searchDao.getFavouriteCities().map {
        it.map { cityDbModel ->
            searchMapper.mapCityDbModelToCity(cityDbModel)
        }
    }

    override fun observeIsFavourite(cityId: Int): Flow<Boolean> =
        searchDao.observeIsFavourite(cityId)


    override suspend fun addToFavourite(city: City) {
        val cityDbModel = searchMapper.mapCityToCityDbModel(city)
        searchDao.addToFavourite(cityDbModel)
    }

    override suspend fun removeFromFavourite(cityId: Int) {
        searchDao.removeFromFavourites(cityId)
    }
}