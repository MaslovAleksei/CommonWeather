package com.margarin.commonweather.weather

import android.content.Context
import com.margarin.commonweather.DD_MM
import com.margarin.commonweather.HH
import com.margarin.commonweather.HH_MM
import com.margarin.commonweather.YYYY_MM_DD
import com.margarin.commonweather.YYYY_MM_DD_HH_MM
import com.margarin.commonweather.local.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.local.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.local.dbmodels.CurrentWeatherDbModel
import com.margarin.commonweather.remote.apimodels.forecast.ForecastData
import com.margarin.commonweather.remote.apimodels.forecast.ForecastDay
import com.margarin.commonweather.remote.apimodels.forecast.Hour
import com.margarin.commonweather.weather.models.ByDaysWeatherModel
import com.margarin.commonweather.weather.models.ByHoursWeatherModel
import com.margarin.commonweather.weather.models.CurrentWeatherModel
import com.margarin.data.R
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherMapper @Inject constructor(
    private val context: Context,
    private val converter: WeatherConverter
) {

    internal fun mapForecastDataToCurrentDbModel(forecastData: ForecastData) =
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
        id: Int, name: String, currentTime: String, hour: Hour
    ) = ByHoursWeatherDbModel(
        name = name,
        id = id,
        time = hour.time,
        currentTime = currentTime,
        temp_c = hour.temp_c?.roundToInt(),
        icon_url = hour.condition?.icon,
        wind_kph = hour.wind_kph?.roundToInt()
    )

    internal fun mapCurrentDbToEntity(db: CurrentWeatherDbModel?): CurrentWeatherModel? {
        return if (db != null) {
            CurrentWeatherModel(
                name = db.name,
                condition = db.condition,
                icon_url = converter.convertConditionImage(db.icon_url.toString()),
                last_updated = converter.createStringLastUpdated(db.last_updated.toString()),
                wind_kph = "${db.wind_kph} ${context.getString(R.string.km_h)}",
                wind_dir = converter.convertWindDirections(db.wind_dir),
                wind_dir_img = converter.bindWindImage(db.wind_dir),
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

    internal fun mapByDaysDbModelToEntity(db: ByDaysWeatherDbModel) =
        ByDaysWeatherModel(
            name = db.name,
            id = db.id,
            date = converter.convertDate(db.date, YYYY_MM_DD, DD_MM),
            maxtemp_c = "${db.maxtemp_c}°",
            mintemp_c = "${db.mintemp_c}°",
            condition = db.condition,
            icon_url = converter.convertConditionImage(db.icon_url.toString()),
            maxwind_kph = "${db.maxwind_kph} ${context.getString(R.string.km_h)}",
            chance_of_rain = "${db.chance_of_rain}%",
            day_of_week = db.date
        )

    internal fun mapByHoursDbModelToEntity(db: ByHoursWeatherDbModel): ByHoursWeatherModel {
        val hourLastUpd = converter.convertDate(db.currentTime, YYYY_MM_DD_HH_MM, HH)
        val hour = converter.convertDate(db.time, YYYY_MM_DD_HH_MM, HH)
        val time = if (hourLastUpd == hour) {
            context.getString(R.string.now)
        } else {
            converter.convertDate(db.time, YYYY_MM_DD_HH_MM, HH_MM)
        }
        return ByHoursWeatherModel(
            name = db.name,
            id = db.id,
            time = time,
            temp_c = "${db.temp_c}°",
            icon_url = converter.convertConditionImage(db.icon_url.toString()),
            wind_kph = "${db.wind_kph} ${context.getString(R.string.km_h)}"
        )
    }

    internal fun mapForecastDataToListDayDbModel(forecastData: ForecastData): List<ByDaysWeatherDbModel> {
        val dayList = mutableListOf<ByDaysWeatherDbModel>()
        forecastData.forecast?.forecastday?.forEachIndexed { index, forecastDay ->
            val day = mapByDayDtoToByDaysDbModel(
                index,
                forecastData.location?.name.toString(),
                forecastDay,
            )
            dayList.add(day)
        }
        return dayList
    }

    internal fun mapForecastDataToListHoursDbModel(forecastData: ForecastData): List<ByHoursWeatherDbModel> {
        val hourList = mutableListOf<ByHoursWeatherDbModel>()
        forecastData.forecast?.forecastday?.first()?.hour?.forEachIndexed { index, forecastHour ->
            val hour = mapByHourDtoToByHoursDbModel(
                index,
                forecastData.location?.name.toString(),
                forecastData.current?.last_updated.toString(),
                forecastHour
            )
            hourList.add(hour)
        }
        return hourList
    }
}