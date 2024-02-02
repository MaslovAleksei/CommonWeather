package com.margarin.commonweather.search

import com.margarin.commonweather.local.dbmodels.CityDbModel
import com.margarin.commonweather.remote.apimodels.search.CityDto
import javax.inject.Inject

class SearchMapper @Inject constructor() {

    internal fun mapCityDtoToCity(cityDto: CityDto) = City(
        id = cityDto.id,
        name = cityDto.name,
        region = cityDto.region,
        country = cityDto.country,
        lat = cityDto.lat,
        lon = cityDto.lon,
        url = cityDto.url
    )

    internal fun mapCityToCityDbModel(city: City) =

        CityDbModel(
            id = city.id,
            name = city.name,
            region = city.region,
            country = city.country,
            lat = city.lat,
            lon = city.lon,
            url = city.url
        )

    internal fun mapCityDbModelToCity(cityDb: CityDbModel) = City(
        id = cityDb.id,
        name = cityDb.name,
        region = cityDb.region,
        country = cityDb.country,
        lat = cityDb.lat,
        lon = cityDb.lon,
        url = cityDb.url
    )
}