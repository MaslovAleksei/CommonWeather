package com.margarin.commonweather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.data.database.dbmodels.CurrentWeatherDbModel

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrentWeather(currentWeatherDbModel: CurrentWeatherDbModel)

    @Query("SELECT * FROM weather_current WHERE name=:name")
    suspend fun getCurrentWeather(name: String): CurrentWeatherDbModel?

}