package com.margarin.commonweather.data

import android.app.Application
import com.margarin.commonweather.DD
import com.margarin.commonweather.HH_MM
import com.margarin.commonweather.MM
import com.margarin.commonweather.YYYY_MM_DD_HH_MM
import com.margarin.weather.R
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class WeatherConverter @Inject constructor (private val application: Application) {

    fun convertConditionImage(url: String): Int? {
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

    fun convertWindDirections(dir: String?): String? {
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

    fun bindWindImage(dir: String?): Int? {
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

    private fun convertMonthNumberToName(number: String): String {
        return when (number) {
            "01" -> application.getString(R.string.january)
            "02" -> application.getString(R.string.february)
            "03" -> application.getString(R.string.march)
            "04" -> application.getString(R.string.april)
            "05" -> application.getString(R.string.may)
            "06" -> application.getString(R.string.june)
            "07" -> application.getString(R.string.july)
            "08" -> application.getString(R.string.august)
            "09" -> application.getString(R.string.september)
            "10" -> application.getString(R.string.october)
            "11" -> application.getString(R.string.november)
            "12" -> application.getString(R.string.december)
            else -> ""
        }
    }

    fun createStringLastUpdated(date: String): String {
        val day = convertDate(date, YYYY_MM_DD_HH_MM, DD)
        val month = convertMonthNumberToName(
            convertDate(date, YYYY_MM_DD_HH_MM, MM) ?: ""
        )
        val time = convertDate(date, YYYY_MM_DD_HH_MM, HH_MM)
        val string = "${application.getString(R.string.updated)} $day " +
                "$month ${application.getString(R.string.at)} $time"
        return string
    }

    fun convertDate(
        serverDate: String?, formatIn: String, formatOut: String
    ): String? {
        val originalFormat = SimpleDateFormat(formatIn, Locale.getDefault())
        val targetFormat = SimpleDateFormat(formatOut, Locale.getDefault())
        val date = originalFormat.parse(serverDate.toString())
        return date?.let { targetFormat.format(it) }
    }

    companion object {

        private const val URL = "//cdn.weatherapi.com/weather/64x64/"
    }
}