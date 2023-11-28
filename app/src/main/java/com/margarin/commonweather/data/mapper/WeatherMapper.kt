package com.margarin.commonweather.data.mapper

import android.annotation.SuppressLint
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.CurrentWeatherDbModel
import com.margarin.commonweather.data.remote.apimodels.current.CurrentData
import com.margarin.commonweather.data.remote.apimodels.forecast.Day
import com.margarin.commonweather.data.remote.apimodels.forecast.Hour
import com.margarin.commonweather.domain.models.ByDaysWeatherModel
import com.margarin.commonweather.domain.models.ByHoursWeatherModel
import com.margarin.commonweather.domain.models.CurrentWeatherModel
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherMapper @Inject constructor() {

    fun mapCurrentDataToCurrentDbModel(dto: CurrentData) = CurrentWeatherDbModel(
        id = UNDEFINED_ID,
        condition = dto.current.condition.text,
        icon_url = dto.current.condition.icon,
        last_updated = dto.current.last_updated,
        wind_kph = dto.current.wind_kph.roundToInt(),
        wind_dir = dto.current.wind_dir,
        temp_c = dto.current.temp_c.roundToInt(),
        pressure_mb = dto.current.pressure_mb.roundToInt(),
        humidity = dto.current.humidity,
        uv = dto.current.uv.roundToInt(),
        feels_like = dto.current.feelslike_c.roundToInt(),
        location = dto.location.name,
        latitude = dto.location.lat,
        longitude = dto.location.lon
    )

    fun mapForecastDataToByDaysDbModel(day: Day, date: String) = ByDaysWeatherDbModel(
        id = UNDEFINED_ID,
        date = date,
        maxtemp_c = day.maxtemp_c.roundToInt(),
        mintemp_c = day.mintemp_c.roundToInt(),
        condition = day.condition.text,
        icon_url = day.condition.icon,
        maxwind_kph = day.maxwind_kph.roundToInt(),
        chance_of_rain = day.daily_chance_of_rain
    )

    fun mapForecastDataToByDaysDbModel(hour: Hour) = ByHoursWeatherDbModel(
        id = UNDEFINED_ID,
        time = hour.time,
        temp_c = hour.temp_c.roundToInt(),
        icon_url = hour.condition.icon,
        wind_kph = hour.wind_kph.roundToInt()
    )

    fun mapCurrentDbToEntity(db: CurrentWeatherDbModel) = CurrentWeatherModel (
        id = db.id,
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
        location = db.location,
        latitude = db.latitude,
        longitude = db.longitude
    )

    fun mapByDaysDbModelToEntity(db: ByDaysWeatherDbModel) = ByDaysWeatherModel(
        id = db.id,
        date = bindDate(db.date),
        maxtemp_c = db.maxtemp_c,
        mintemp_c = db.mintemp_c,
        condition = db.condition,
        icon_url = db.icon_url,
        maxwind_kph = db.maxwind_kph,
        chance_of_rain = db.chance_of_rain,
        convertDateToDayOfWeek(db.date)
    )

    fun mapByHoursDbModelToEntity(db: ByHoursWeatherDbModel) = ByHoursWeatherModel(
        id = db.id,
        time = db.time,
        temp_c = db.temp_c,
        icon_url = db.icon_url,
        wind_kph = db.wind_kph
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

    private fun bindDate(date: String) = when (
        date.substring(5, 7).toInt()) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        else -> "December"
    }.plus(" " + date.drop(8).toInt())

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate() = SimpleDateFormat(TIME_FORMAT).format(Date())

    companion object{
        private const val UNDEFINED_ID = 0L
        private const val TIME_FORMAT = "dd-MM-yyyy"
    }
}