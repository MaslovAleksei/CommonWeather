package com.margarin.commonweather.data

import com.margarin.commonweather.apimodels.search.Search
import com.margarin.commonweather.dbmodels.SearchDbModel
import com.margarin.commonweather.domain.SearchModel
import javax.inject.Inject

class SearchMapper @Inject constructor() {

    internal fun mapSearchDtoToSearchModel(search: Search) = SearchModel(
        id = search.id,
        name = search.name,
        region = search.region,
        country = search.country,
        lat = search.lat,
        lon = search.lon,
        url = search.url
    )

    internal fun mapSearchModelToSearchDbModel(searchModel: SearchModel) =
        SearchDbModel(
            id = searchModel.id,
            name = searchModel.name,
            region = searchModel.region,
            country = searchModel.country,
            lat = searchModel.lat,
            lon = searchModel.lon,
            url = searchModel.url
        )

    internal fun mapSearchDbModelToSearchModel(searchDb: SearchDbModel) =
        SearchModel(
            id = searchDb.id,
            name = searchDb.name,
            region = searchDb.region,
            country = searchDb.country,
            lat = searchDb.lat,
            lon = searchDb.lon,
            url = searchDb.url
        )
}