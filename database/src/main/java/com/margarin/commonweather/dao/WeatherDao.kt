package com.margarin.commonweather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.dbmodels.CurrentWeatherDbModel

@Dao
interface WeatherDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrentWeather(currentWeatherDbModel: CurrentWeatherDbModel)

    @Query("SELECT * FROM weather_current WHERE name=:name")
    suspend fun getCurrentWeather(name: String): CurrentWeatherDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addByDaysWeather(byDaysWeatherDbModel: List<ByDaysWeatherDbModel>)

    @Query("SELECT * FROM weather_by_days WHERE name=:name")
    suspend fun getByDaysWeather(name: String): List<ByDaysWeatherDbModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addByHoursWeather(byHoursWeatherDbModel: List<ByHoursWeatherDbModel>)

    @Query("SELECT * FROM weather_by_hours WHERE name=:name")
    suspend fun getByHoursWeather(name: String): List<ByHoursWeatherDbModel>?

}