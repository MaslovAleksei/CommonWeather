package com.margarin.commonweather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.WeatherDbModel

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherDbModel: WeatherDbModel)

    @Query("SELECT * FROM weather_table WHERE name=:name")
    suspend fun getWeather(name: String): WeatherDbModel?

}