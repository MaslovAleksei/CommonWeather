package com.margarin.commonweather.data.mapper

import android.annotation.SuppressLint
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.CurrentWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.SearchDbModel
import com.margarin.commonweather.data.remote.apimodels.forecast.Day
import com.margarin.commonweather.data.remote.apimodels.forecast.ForecastData
import com.margarin.commonweather.data.remote.apimodels.forecast.Hour
import com.margarin.commonweather.data.remote.apimodels.search.Search
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.data.R
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherMapper @Inject constructor() {

    fun mapForecastDataToCurrentDbModel(forecastData: ForecastData) = CurrentWeatherDbModel(
        name = forecastData.location?.name.toString(),
        condition = forecastData.current?.condition?.text,
        icon_url = mapConditionImage(
            forecastData.current?.condition?.icon.toString(),
            forecastData.current?.is_day.toString()
        ),
        last_updated = forecastData.current?.last_updated,
        wind_kph = forecastData.current?.wind_kph?.roundToInt(),
        wind_dir = forecastData.current?.wind_dir,
        temp_c = forecastData.current?.temp_c?.roundToInt(),
        pressure_mb = forecastData.current?.pressure_mb?.roundToInt(),
        humidity = forecastData.current?.humidity,
        uv = forecastData.current?.uv?.roundToInt(),
        feels_like = forecastData.current?.feelslike_c?.roundToInt(),
        latitude = forecastData.location?.lat,
        longitude = forecastData.location?.lon
    )


    private fun mapByDayDtoToByDaysDbModel(id: Int, name: String, day: Day) = ByDaysWeatherDbModel(
        name = name,
        id = id,
        date = id.toString(),
        maxtemp_c = day.maxtemp_c?.roundToInt(),
        mintemp_c = day.mintemp_c?.roundToInt(),
        condition = day.condition?.text,
        icon_url = mapConditionImage(day.condition?.icon.toString(), IS_DAY),
        maxwind_kph = day.maxwind_kph?.roundToInt(),
        chance_of_rain = day.daily_chance_of_rain,
    )

    private fun mapByHourDtoToByHoursDbModel(id: Int, name: String, hour: Hour) =
        ByHoursWeatherDbModel(
            name = name,
            id = id,
            time = hour.time,
            temp_c = hour.temp_c?.roundToInt(),
            icon_url = mapConditionImage(hour.condition?.icon.toString(), hour.is_day.toString()),
            wind_kph = hour.wind_kph?.roundToInt()
        )

    fun mapCurrentDbToEntity(db: CurrentWeatherDbModel?): CurrentWeatherModel? {
        return if (db != null) {
            CurrentWeatherModel(
                name = db.name,
                condition = db.condition,
                icon_url = db.icon_url,
                last_updated = db.last_updated,
                wind_kph = db.wind_kph,
                wind_dir = db.wind_dir,
                temp_c = db.temp_c,
                pressure_mb = db.pressure_mb,
                humidity = db.humidity,
                uv = db.uv,
                feels_like = db.feels_like,
                latitude = db.latitude,
                longitude = db.longitude
            )
        } else {
            null
        }
    }

    fun mapByDaysDbModelToEntity(db: ByDaysWeatherDbModel) = ByDaysWeatherModel(
        name = db.name,
        id = db.id,
        date = db.date,
        maxtemp_c = db.maxtemp_c,
        mintemp_c = db.mintemp_c,
        condition = db.condition,
        icon_url = db.icon_url,
        maxwind_kph = db.maxwind_kph,
        chance_of_rain = db.chance_of_rain,
        day_of_week = db.date
    )

    fun mapByHoursDbModelToEntity(db: ByHoursWeatherDbModel) = ByHoursWeatherModel(
        name = db.name,
        id = db.id,
        time = db.time,
        temp_c = db.temp_c,
        icon_url = db.icon_url,
        wind_kph = db.wind_kph
    )


    fun mapForecastDataToListDayDbModel(forecastData: ForecastData): List<ByDaysWeatherDbModel> {
        val dayList = mutableListOf<ByDaysWeatherDbModel>()
        for (i in 0 until forecastData.forecast?.forecastday?.size!!) {
            val day = mapByDayDtoToByDaysDbModel(
                i,
                forecastData.location!!.name,
                forecastData.forecast.forecastday[i].day!!,
            )
            dayList.add(day)
        }
        return dayList
    }

    fun mapForecastDataToListHoursDbModel(forecastData: ForecastData): List<ByHoursWeatherDbModel> {
        val hourList = mutableListOf<ByHoursWeatherDbModel>()
        for (i in 0 until forecastData.forecast?.forecastday?.first()?.hour?.size!!) {
            val hour = mapByHourDtoToByHoursDbModel(
                i,
                forecastData.location!!.name,
                forecastData.forecast.forecastday.first().hour!![i]

            )
            hourList.add(hour)
        }

        return hourList
    }

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

    private fun mapConditionImage(url: String, isDay: String): Int {
        if (isDay == IS_DAY) {
            return when (url) {
                URL + "113.png" -> R.drawable.ic_sun_icons2
                URL + "116.png" -> R.drawable.ic_icons_sun_behind_clouds
                URL + "119.png" -> R.drawable.ic_cloud_icons2
                URL + "122.png" -> R.drawable.ic_cloud_icons2
                URL + "143.png" -> R.drawable.ic_cloud_icons2 //TODO
                URL + "176.png" -> R.drawable.ic_icons_of_the_sun_behind_the_rainy_cloud
                URL + "179.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "182.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "185.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "200.png" -> R.drawable.ic_thunderclouds_icons
                URL + "227.png" -> R.drawable.ic_snow_cloud_icons
                URL + "230.png" -> R.drawable.ic_snow_cloud_icons
                URL + "248.png" -> R.drawable.ic_cloud_icons2 //TODO
                URL + "260.png" -> R.drawable.ic_cloud_icons2 //TODO
                URL + "263.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "266.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "281.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "284.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "293.png" -> R.drawable.ic_icons_of_the_sun_behind_the_rainy_cloud
                URL + "296.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "299.png" -> R.drawable.ic_icons_of_the_sun_behind_the_rainy_cloud
                URL + "302.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "305.png" -> R.drawable.ic_icons_of_the_sun_behind_the_rainy_cloud
                URL + "308.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "311.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "314.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "317.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "320.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "323.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "326.png" -> R.drawable.ic_snow_cloud_icons
                URL + "329.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "332.png" -> R.drawable.ic_snow_cloud_icons
                URL + "335.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "338.png" -> R.drawable.ic_snow_cloud_icons
                URL + "350.png" -> R.drawable.ic_hail2
                URL + "353.png" -> R.drawable.ic_icons_of_the_sun_behind_the_rainy_cloud
                URL + "356.png" -> R.drawable.ic_icons_of_the_sun_behind_the_rainy_cloud
                URL + "359.png" -> R.drawable.ic_icons_of_the_sun_behind_the_rainy_cloud
                URL + "362.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "365.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "368.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "371.png" -> R.drawable.ic_icons_of_the_sun_behind_the_snow_cloud2
                URL + "374.png" -> R.drawable.ic_hail2
                URL + "377.png" -> R.drawable.ic_hail2
                URL + "386.png" -> R.drawable.ic_heavy_rain_icons2
                URL + "389.png" -> R.drawable.ic_heavy_rain_icons2
                URL + "392.png" -> R.drawable.ic_thunderclouds_icons
                URL + "395.png" -> R.drawable.ic_thunderclouds_icons
                else -> R.drawable.ic_hot_temperature_icons2
            }
        } else {
            return when (url) {
                URL + "113.png" -> R.drawable.ic_moon_and_stars_icons2
                URL + "116.png" -> R.drawable.ic_icons_of_the_moon_behind_the_cloud
                URL + "119.png" -> R.drawable.ic_icons_of_the_moon_behind_the_cloud
                URL + "122.png" -> R.drawable.ic_cloud_icons2
                URL + "143.png" -> R.drawable.ic_cloud_icons2 //TODO
                URL + "176.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "179.png" -> R.drawable.ic_snow_cloud_icons
                URL + "182.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "185.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "200.png" -> R.drawable.ic_thunderclouds_icons
                URL + "227.png" -> R.drawable.ic_snow_cloud_icons
                URL + "230.png" -> R.drawable.ic_snow_cloud_icons
                URL + "248.png" -> R.drawable.ic_cloud_icons2 //TODO
                URL + "260.png" -> R.drawable.ic_cloud_icons2 //TODO
                URL + "263.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "266.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "281.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "284.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "293.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "296.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "299.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "302.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "305.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "308.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "311.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "314.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "317.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "320.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "323.png" -> R.drawable.ic_snow_cloud_icons
                URL + "326.png" -> R.drawable.ic_snow_cloud_icons
                URL + "329.png" -> R.drawable.ic_snow_cloud_icons
                URL + "332.png" -> R.drawable.ic_snow_cloud_icons
                URL + "335.png" -> R.drawable.ic_snow_cloud_icons
                URL + "338.png" -> R.drawable.ic_snow_cloud_icons
                URL + "350.png" -> R.drawable.ic_hail2
                URL + "353.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "356.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "359.png" -> R.drawable.ic_rainy_cloud_icons2
                URL + "362.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "365.png" -> R.drawable.ic_rain_and_snow_icons
                URL + "368.png" -> R.drawable.ic_snow_cloud_icons
                URL + "371.png" -> R.drawable.ic_snow_cloud_icons
                URL + "374.png" -> R.drawable.ic_hail2
                URL + "377.png" -> R.drawable.ic_hail2
                URL + "386.png" -> R.drawable.ic_heavy_rain_icons2
                URL + "389.png" -> R.drawable.ic_heavy_rain_icons2
                URL + "392.png" -> R.drawable.ic_thunderclouds_icons
                URL + "395.png" -> R.drawable.ic_thunderclouds_icons
                else -> R.drawable.ic_hot_temperature_icons2
            }
        }
    }

    private fun convertDateToDayOfWeek(date: String): String {
        if (date == getCurrentDate()) {
            return "Today"
        }
        val day = date.takeLast(2).toInt()
        val yearLastTwo = date.substring(2, 4).toInt()
        val month = date.substring(5, 7).toInt()
        val yearCode = (6 + yearLastTwo + yearLastTwo / 4) % 7
        val monthCode = when (month) {
            1, 10 -> 1
            12, 9 -> 6
            5 -> 2
            8 -> 3
            2, 3, 11 -> 4
            6 -> 5
            4, 7 -> 0
            else -> 10
        }

        var result = (day + monthCode + yearCode) % 7
        if (yearLastTwo % 4 == 0 && (month == 1 || month == 2)) result -= 1
        return when (result) {
            0 -> "Saturday"
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            else -> "Unknown"
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate() = SimpleDateFormat(TIME_FORMAT).format(Date())

    companion object {
        private const val URL = "//cdn.weatherapi.com/weather/64x64/day/"
        private const val IS_DAY = "1"
        private const val TIME_FORMAT = "dd-MM-yyyy"
    }
}