package com.margarin.commonweather.data

import android.app.Application
import com.margarin.commonweather.apimodels.forecast.ForecastData
import com.margarin.commonweather.apimodels.forecast.ForecastDay
import com.margarin.commonweather.apimodels.forecast.Hour
import com.margarin.commonweather.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.dbmodels.CurrentWeatherDbModel
import com.margarin.weather.R
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherMapper @Inject constructor(
    val application: Application
) {

    fun mapForecastDataToCurrentDbModel(forecastData: ForecastData) =
        CurrentWeatherDbModel(
            name = forecastData.location?.name.toString(),
            condition = forecastData.current?.condition?.text,
            icon_url = forecastData.current?.condition?.icon,
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
            icon_url = forecastDay.day?.condition?.icon.toString(),
            maxwind_kph = forecastDay.day?.maxwind_kph?.roundToInt(),
            chance_of_rain = forecastDay.day?.daily_chance_of_rain,
        )

    private fun mapByHourDtoToByHoursDbModel(
        id: Int,
        name: String,
        currentTime: String,
        hour: Hour
    ) = ByHoursWeatherDbModel(
        name = name,
        id = id,
        time = hour.time,
        currentTime = currentTime,
        temp_c = hour.temp_c?.roundToInt(),
        icon_url = hour.condition?.icon,
        wind_kph = hour.wind_kph?.roundToInt()
    )

    fun mapCurrentDbToEntity(db: CurrentWeatherDbModel?): com.margarin.commonweather.domain.models.CurrentWeatherModel? {
        return if (db != null) {
            com.margarin.commonweather.domain.models.CurrentWeatherModel(
                name = db.name,
                condition = db.condition,
                icon_url = mapConditionImage(db.icon_url.toString()),
                last_updated = application.getString(R.string.updated) +
                        convertDate(db.last_updated, DATE_FULL_DEFAULT, DATE_LAST_UPD),
                wind_kph = "${db.wind_kph} ${application.getString(R.string.km_h)}",
                wind_dir = convertWindDirections(db.wind_dir),
                wind_dir_img = bindWindImage(db.wind_dir),
                temp_c = db.temp_c.toString(),
                pressure_mb = "${db.pressure_mb}",
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

    fun mapByDaysDbModelToEntity(db: ByDaysWeatherDbModel) =
        com.margarin.commonweather.domain.models.ByDaysWeatherModel(
            name = db.name,
            id = db.id,
            date = convertDate(db.date, DATE_DEFAULT, DAY_MONTH),
            maxtemp_c = "${db.maxtemp_c}°",
            mintemp_c = "${db.mintemp_c}°",
            condition = db.condition,
            icon_url = mapConditionImage(db.icon_url.toString()),
            maxwind_kph = "${db.maxwind_kph} ${application.getString(R.string.km_h)}",
            chance_of_rain = "${db.chance_of_rain}%",
            day_of_week = db.date
        )

    fun mapByHoursDbModelToEntity(db: ByHoursWeatherDbModel): com.margarin.commonweather.domain.models.ByHoursWeatherModel {
        val hourLastUpd = convertDate(db.currentTime, DATE_FULL_DEFAULT, HOUR)
        val hour = convertDate(db.time, DATE_FULL_DEFAULT, HOUR)
        val time = if (hourLastUpd == hour) {
            application.getString(R.string.now)
        } else {
            convertDate(db.time, DATE_FULL_DEFAULT, TIME)
        }
        return com.margarin.commonweather.domain.models.ByHoursWeatherModel(
            name = db.name,
            id = db.id,
            time = time,
            temp_c = "${db.temp_c}°",
            icon_url = mapConditionImage(db.icon_url.toString()),
            wind_kph = "${db.wind_kph} ${application.getString(R.string.km_h)}"
        )
    }



    fun mapForecastDataToListDayDbModel(forecastData: ForecastData): List<ByDaysWeatherDbModel> {
        val dayList = mutableListOf<ByDaysWeatherDbModel>()
        for (i in 0 until forecastData.forecast?.forecastday?.size!!) {
            val day = mapByDayDtoToByDaysDbModel(
                i,
                forecastData.location?.name.toString(),
                forecastData.forecast!!.forecastday!![i],
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
                forecastData.location?.name.toString(),
                forecastData.current?.last_updated.toString(),
                forecastData.forecast!!.forecastday!!.first().hour!![i]
            )
            hourList.add(hour)
        }

        return hourList
    }

    private fun mapConditionImage(url: String): Int? {
        return when (url) {
            URL + "day/113.png" -> R.drawable.clear_day
            URL + "day/116.png" -> R.drawable.cloudy_day_1
            URL + "day/119.png" -> R.drawable.cloudy_day_2
            URL + "day/122.png" -> R.drawable.cloudy_day_3
            URL + "day/143.png" -> R.drawable.fog_day
            URL + "day/176.png" -> R.drawable.rainy_1_day
            URL + "day/179.png" -> R.drawable.snowy_1_day
            URL + "day/182.png" -> R.drawable.rain_and_snow_mix
            URL + "day/185.png" -> R.drawable.snow_and_sleet_mix
            URL + "day/200.png" -> R.drawable.scattered_thunderstorms_day
            URL + "day/227.png" -> R.drawable.snowy_2
            URL + "day/230.png" -> R.drawable.snowy_3
            URL + "day/248.png" -> R.drawable.fog
            URL + "day/260.png" -> R.drawable.fog
            URL + "day/263.png" -> R.drawable.rain_and_sleet_mix
            URL + "day/266.png" -> R.drawable.rain_and_sleet_mix
            URL + "day/281.png" -> R.drawable.rain_and_snow_mix
            URL + "day/284.png" -> R.drawable.rain_and_snow_mix
            URL + "day/293.png" -> R.drawable.rainy_1_day
            URL + "day/296.png" -> R.drawable.rainy_1
            URL + "day/299.png" -> R.drawable.rainy_2_day
            URL + "day/302.png" -> R.drawable.rainy_2
            URL + "day/305.png" -> R.drawable.rainy_3_day
            URL + "day/308.png" -> R.drawable.rainy_3
            URL + "day/311.png" -> R.drawable.rain_and_snow_mix
            URL + "day/314.png" -> R.drawable.rain_and_snow_mix
            URL + "day/317.png" -> R.drawable.rain_and_sleet_mix
            URL + "day/320.png" -> R.drawable.snow_and_sleet_mix
            URL + "day/323.png" -> R.drawable.snowy_1_day
            URL + "day/326.png" -> R.drawable.snowy_1
            URL + "day/329.png" -> R.drawable.snowy_2_day
            URL + "day/332.png" -> R.drawable.snowy_2
            URL + "day/335.png" -> R.drawable.snowy_3_day
            URL + "day/338.png" -> R.drawable.snowy_3
            URL + "day/350.png" -> R.drawable.hail
            URL + "day/353.png" -> R.drawable.rainy_1_day
            URL + "day/356.png" -> R.drawable.rainy_2_day
            URL + "day/359.png" -> R.drawable.rainy_3_day
            URL + "day/362.png" -> R.drawable.rain_and_snow_mix
            URL + "day/365.png" -> R.drawable.snow_and_sleet_mix
            URL + "day/368.png" -> R.drawable.snowy_1_day
            URL + "day/371.png" -> R.drawable.snowy_2_day
            URL + "day/374.png" -> R.drawable.hail
            URL + "day/377.png" -> R.drawable.hail
            URL + "day/386.png" -> R.drawable.isolated_thunderstorms_day
            URL + "day/389.png" -> R.drawable.scattered_thunderstorms
            URL + "day/392.png" -> R.drawable.isolated_thunderstorms_day
            URL + "day/395.png" -> R.drawable.scattered_thunderstorms
            URL + "night/113.png" -> R.drawable.clear
            URL + "night/116.png" -> R.drawable.cloudy_night_1
            URL + "night/119.png" -> R.drawable.cloudy_night_2
            URL + "night/122.png" -> R.drawable.cloudy_night_3
            URL + "night/143.png" -> R.drawable.fog_night
            URL + "night/176.png" -> R.drawable.rainy_1_night
            URL + "night/179.png" -> R.drawable.snowy_1_night
            URL + "night/182.png" -> R.drawable.rain_and_snow_mix
            URL + "night/185.png" -> R.drawable.snow_and_sleet_mix
            URL + "night/200.png" -> R.drawable.scattered_thunderstorms_night
            URL + "night/227.png" -> R.drawable.snowy_2
            URL + "night/230.png" -> R.drawable.snowy_3
            URL + "night/248.png" -> R.drawable.fog
            URL + "night/260.png" -> R.drawable.fog
            URL + "night/263.png" -> R.drawable.rain_and_sleet_mix
            URL + "night/266.png" -> R.drawable.rain_and_sleet_mix
            URL + "night/281.png" -> R.drawable.rain_and_snow_mix
            URL + "night/284.png" -> R.drawable.rain_and_snow_mix
            URL + "night/293.png" -> R.drawable.rainy_1_night
            URL + "night/296.png" -> R.drawable.rainy_1
            URL + "night/299.png" -> R.drawable.rainy_2_night
            URL + "night/302.png" -> R.drawable.rainy_2
            URL + "night/305.png" -> R.drawable.rainy_3_night
            URL + "night/308.png" -> R.drawable.rainy_3
            URL + "night/311.png" -> R.drawable.rain_and_snow_mix
            URL + "night/314.png" -> R.drawable.rain_and_snow_mix
            URL + "night/317.png" -> R.drawable.rain_and_sleet_mix
            URL + "night/320.png" -> R.drawable.snow_and_sleet_mix
            URL + "night/323.png" -> R.drawable.snowy_1_night
            URL + "night/326.png" -> R.drawable.snowy_1
            URL + "night/329.png" -> R.drawable.snowy_2_night
            URL + "night/332.png" -> R.drawable.snowy_2
            URL + "night/335.png" -> R.drawable.snowy_3_night
            URL + "night/338.png" -> R.drawable.snowy_3
            URL + "night/350.png" -> R.drawable.hail
            URL + "night/353.png" -> R.drawable.rainy_1_night
            URL + "night/356.png" -> R.drawable.rainy_2_night
            URL + "night/359.png" -> R.drawable.rainy_3_night
            URL + "night/362.png" -> R.drawable.rain_and_snow_mix
            URL + "night/365.png" -> R.drawable.snow_and_sleet_mix
            URL + "night/368.png" -> R.drawable.snowy_1_night
            URL + "night/371.png" -> R.drawable.snowy_2_night
            URL + "night/374.png" -> R.drawable.hail
            URL + "night/377.png" -> R.drawable.hail
            URL + "night/386.png" -> R.drawable.isolated_thunderstorms_night
            URL + "night/389.png" -> R.drawable.scattered_thunderstorms
            URL + "night/392.png" -> R.drawable.isolated_thunderstorms_night
            URL + "night/395.png" -> R.drawable.scattered_thunderstorms
            else -> null
        }
    }

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
            "W" -> R.drawable.ic_dir_west
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
        val originalFormat = SimpleDateFormat(formatIn, Locale.getDefault())
        val targetFormat = SimpleDateFormat(formatOut, Locale.getDefault())
        val date = originalFormat.parse(serverDate.toString())
        return date?.let { targetFormat.format(it) }
    }

    companion object {
        private const val URL = "//cdn.weatherapi.com/weather/64x64/"
        private const val DATE_DEFAULT = "yyyy-MM-dd"
        private const val DATE_FULL_DEFAULT = "yyyy-MM-dd HH:mm"
        private const val DAY_MONTH = "dd.MM"
        private const val TIME = "HH:mm"
        private const val HOUR = "HH"
        private const val DATE_LAST_UPD = "dd.MM HH:mm"


    }
}