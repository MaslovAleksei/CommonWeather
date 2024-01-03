package com.margarin.commonweather.data.mapper

import android.app.Application
import com.margarin.commonweather.data.database.dbmodels.SearchDbModel
import com.margarin.commonweather.data.api.apimodels.search.Search
import com.margarin.commonweather.domain.models.SearchModel
import javax.inject.Inject

class SearchMapper @Inject constructor(
    val application: Application
) {

    fun mapSearchDtoToSearchModel(search: Search) = SearchModel(
        id = search.id,
        name = search.name,
        region = search.region,
        country = search.country,
        lat = search.lat,
        lon = search.lon,
        url = search.url
    )

    fun mapSearchModelToSearchDbModel(searchModel: SearchModel) = SearchDbModel(
        id = searchModel.id,
        name = searchModel.name,
        region = searchModel.region,
        country = searchModel.country,
        lat = searchModel.lat,
        lon = searchModel.lon,
        url = searchModel.url
    )

    fun mapSearchDbModelToSearchModel(searchDb: SearchDbModel) = SearchModel(
        id = searchDb.id,
        name = searchDb.name,
        region = searchDb.region,
        country = searchDb.country,
        lat = searchDb.lat,
        lon = searchDb.lon,
        url = searchDb.url
    )
}