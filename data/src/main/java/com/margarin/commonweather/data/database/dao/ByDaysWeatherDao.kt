package com.margarin.commonweather.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel

@Dao
interface ByDaysWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertByDaysWeather(byDaysWeatherDbModel: List<ByDaysWeatherDbModel>)

    @Query("SELECT * FROM weather_by_days")
    fun loadByDaysWeather(): LiveData<List<ByDaysWeatherDbModel>>

}