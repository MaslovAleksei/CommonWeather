package com.margarin.commonweather.data.mapper

import android.app.Application
import android.util.Log
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.CurrentWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.SearchDbModel
import com.margarin.commonweather.data.remote.apimodels.forecast.ForecastData
import com.margarin.commonweather.data.remote.apimodels.forecast.ForecastDay
import com.margarin.commonweather.data.remote.apimodels.forecast.Hour
import com.margarin.commonweather.data.remote.apimodels.search.Search
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel
import com.margarin.data.R
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherMapper @Inject constructor(
    val application: Application
) {

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


    private fun mapByDayDtoToByDaysDbModel(id: Int, name: String, forecastDay: ForecastDay) =
        ByDaysWeatherDbModel(
            name = name,
            id = id,
            date = forecastDay.date,
            maxtemp_c = forecastDay.day?.maxtemp_c?.roundToInt(),
            mintemp_c = forecastDay.day?.mintemp_c?.roundToInt(),
            condition = forecastDay.day?.condition?.text,
            icon_url = mapConditionImage(
                forecastDay.toString(),
                forecastDay.toString()
            ),
            maxwind_kph = forecastDay.day?.maxwind_kph?.roundToInt(),
            chance_of_rain = forecastDay.day?.daily_chance_of_rain,
        )

    private fun mapByHourDtoToByHoursDbModel(id: Int, name: String, hour: Hour) =
        ByHoursWeatherDbModel(
            name = name,
            id = id,
            time = hour.time,
            temp_c = hour.temp_c?.roundToInt(),
            icon_url = mapConditionImage(
                hour.toString(),
                hour.toString()
            ),
            wind_kph = hour.wind_kph?.roundToInt()
        )

    fun mapCurrentDbToEntity(db: CurrentWeatherDbModel?): CurrentWeatherModel? {
        return if (db != null) {
            CurrentWeatherModel(
                name = db.name,
                condition = db.condition,
                icon_url = R.drawable.clear_day,
                last_updated = application.getString(R.string.updated) +
                        convertDate(db.last_updated, DATE_WITH_TIME_DEFAULT, DATE_LAST_UPD),
                wind_kph = "${db.wind_kph} ${application.getString(R.string.km_h)}",
                wind_dir = convertWindDirections(db.wind_dir),
                wind_dir_img = bindWindImage(db.wind_dir),
                temp_c = db.temp_c.toString(),
                pressure_mb = "${db.pressure_mb}${application.getString(R.string.mbar)}",
                humidity = "${db.wind_kph}%",
                uv = db.uv,
                feels_like = "${db.feels_like}°C",
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
        date = convertDate(db.date, DATE_DEFAULT, DATE_DAILY),
        maxtemp_c = "${db.maxtemp_c}°",
        mintemp_c = "${db.mintemp_c}°",
        condition = db.condition,
        icon_url = db.icon_url,
        maxwind_kph = "${db.maxwind_kph} ${application.getString(R.string.km_h)}",
        chance_of_rain = "${db.chance_of_rain}%",
        day_of_week = db.date
    )

    fun mapByHoursDbModelToEntity(db: ByHoursWeatherDbModel) = ByHoursWeatherModel(
        name = db.name,
        id = db.id,
        time = db.time,
        temp_c = "${db.temp_c}°",
        icon_url = db.icon_url,
        wind_kph = "${db.wind_kph} ${application.getString(R.string.km_h)}"
    )


    fun mapForecastDataToListDayDbModel(forecastData: ForecastData): List<ByDaysWeatherDbModel> {
        val dayList = mutableListOf<ByDaysWeatherDbModel>()
        for (i in 0 until forecastData.forecast?.forecastday?.size!!) {
            val day = mapByDayDtoToByDaysDbModel(
                i,
                forecastData.location!!.name,
                forecastData.forecast.forecastday[i],
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
        return R.drawable.ic_dir_south
    }
        /*
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

     */
    
    private fun convertWindDirections(dir: String?): String? {
        return when (dir) {
            "S" -> application.getString(R.string.south)
            "N" -> application.getString(R.string.north)
            "W" -> application.getString(R.string.west)
            "E" -> application.getString(R.string.east)
            "SE" -> application.getString(R.string.south_east)
            "ESE" -> application.getString(R.string.south_east)
            "SSE" -> application.getString(R.string.south_east)
            "SW" -> application.getString(R.string.south_west)
            "WSW" -> application.getString(R.string.south_west)
            "SSW" -> application.getString(R.string.south_west)
            "NE" -> application.getString(R.string.north_east)
            "NNE" -> application.getString(R.string.north_east)
            "ENE" -> application.getString(R.string.north_east)
            "NW" -> application.getString(R.string.north_west)
            "WNW" -> application.getString(R.string.north_west)
            "NNW" -> application.getString(R.string.north_west)
            else -> null
        }
    }

    private fun bindWindImage(dir: String?): Int? {
        return when (dir) {
            "S" -> R.drawable.ic_dir_south
            "N" -> R.drawable.ic_dir_north
            "W" -> R.drawable.ic_dir_north_west
            "E" -> R.drawable.ic_dir_east
            "SE" -> R.drawable.ic_dir_south_east
            "ESE" -> R.drawable.ic_dir_south_east
            "SSE" -> R.drawable.ic_dir_south_east
            "SW" -> R.drawable.ic_dir_south_west
            "WSW" -> R.drawable.ic_dir_south_west
            "SSW" -> R.drawable.ic_dir_south_west
            "NE" -> R.drawable.ic_dir_north_east
            "NNE" -> R.drawable.ic_dir_north_east
            "ENE" -> R.drawable.ic_dir_north_east
            "NW" -> R.drawable.ic_dir_north_west
            "WNW" -> R.drawable.ic_dir_north_west
            "NNW" -> R.drawable.ic_dir_north_west
            else -> null
        }
    }

    private fun convertDate(
        serverDate: String?,
        formatIn: String,
        formatOut: String
    ): String? {
        Log.d("tag", serverDate + formatIn + formatOut)
        val originalFormat = SimpleDateFormat(formatIn, Locale.getDefault())
        val targetFormat = SimpleDateFormat(formatOut, Locale.getDefault())
        val date = originalFormat.parse(serverDate.toString())
        return date?.let { targetFormat.format(it) }
    }

    companion object {
        private const val URL = "//cdn.weatherapi.com/weather/64x64/day/"
        private const val IS_DAY = "1"
        private const val DATE_DEFAULT = "yyyy-MM-dd"
        private const val DATE_WITH_TIME_DEFAULT = "yyyy-MM-dd HH:mm"
        private const val DATE_DAILY  = "dd.MM"
        private const val DATE_HOURLY  = "dd.MM"
        private const val DATE_LAST_UPD  = "dd.MM HH:mm"


    }
}