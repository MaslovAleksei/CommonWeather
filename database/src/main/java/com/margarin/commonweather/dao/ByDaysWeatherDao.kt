package com.margarin.commonweather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.dbmodels.ByDaysWeatherDbModel

@Dao
interface ByDaysWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addByDaysWeather(byDaysWeatherDbModel: List<ByDaysWeatherDbModel>)

    @Query("SELECT * FROM weather_by_days WHERE name=:name")
    suspend fun getByDaysWeather(name: String): List<ByDaysWeatherDbModel>?

}