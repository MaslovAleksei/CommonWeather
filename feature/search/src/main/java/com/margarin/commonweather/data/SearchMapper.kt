package com.margarin.commonweather.data

import android.app.Application
import com.margarin.commonweather.apimodels.search.Search
import com.margarin.commonweather.dbmodels.SearchDbModel
import com.margarin.commonweather.domain.SearchModel
import javax.inject.Inject

class SearchMapper @Inject constructor(
    val application: Application
) {

    fun mapSearchDtoToSearchModel(search: Search) = com.margarin.commonweather.domain.SearchModel(
        id = search.id,
        name = search.name,
        region = search.region,
        country = search.country,
        lat = search.lat,
        lon = search.lon,
        url = search.url
    )

    fun mapSearchModelToSearchDbModel(searchModel: com.margarin.commonweather.domain.SearchModel) =
        SearchDbModel(
            id = searchModel.id,
            name = searchModel.name,
            region = searchModel.region,
            country = searchModel.country,
            lat = searchModel.lat,
            lon = searchModel.lon,
            url = searchModel.url
        )

    fun mapSearchDbModelToSearchModel(searchDb: SearchDbModel) =
        com.margarin.commonweather.domain.SearchModel(
            id = searchDb.id,
            name = searchDb.name,
            region = searchDb.region,
            country = searchDb.country,
            lat = searchDb.lat,
            lon = searchDb.lon,
            url = searchDb.url
        )
}