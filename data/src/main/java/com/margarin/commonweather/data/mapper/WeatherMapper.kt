package com.margarin.commonweather.data.mapper

import android.annotation.SuppressLint
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.CurrentWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.SearchDbModel
import com.margarin.commonweather.data.remote.apimodels.current.CurrentData
import com.margarin.commonweather.data.remote.apimodels.forecast.Day
import com.margarin.commonweather.data.remote.apimodels.forecast.ForecastData
import com.margarin.commonweather.data.remote.apimodels.forecast.Hour
import com.margarin.commonweather.data.remote.apimodels.search.Search
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import com.margarin.commonweather.domain.models.SearchModel
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherMapper @Inject constructor() {

    fun mapForecastDataToCurrentDbModel(forecastData: ForecastData) = CurrentWeatherDbModel(
        name = forecastData.location?.name.toString(),
        condition = forecastData.current?.condition?.text,
        icon_url = HTTPS + forecastData.current?.condition?.icon,
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


    fun mapByDayDtoToByDaysDbModel(id: Int, name: String, day: Day) = ByDaysWeatherDbModel(
        name = name,
        id = id,
        date = id.toString(),
        maxtemp_c = day.maxtemp_c?.roundToInt(),
        mintemp_c = day.mintemp_c?.roundToInt(),
        condition = day.condition?.text,
        icon_url = HTTPS + day.condition?.icon,
        maxwind_kph = day.maxwind_kph?.roundToInt(),
        chance_of_rain = day.daily_chance_of_rain,
    )

    fun mapByHourDtoToByHoursDbModel(id: Int, name: String, hour: Hour) = ByHoursWeatherDbModel(
        name = name,
        id = id,
        time = hour.time,
        temp_c = hour.temp_c?.roundToInt(),
        icon_url = HTTPS + hour.condition?.icon,
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
        private const val TIME_FORMAT = "dd-MM-yyyy"
        private const val HTTPS = "https:"
    }
}