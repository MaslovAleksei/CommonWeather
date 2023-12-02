package com.margarin.commonweather.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.data.database.dbmodels.ByHoursWeatherDbModel

@Dao
interface ByHoursWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertByHoursWeather(byHoursWeatherDbModel: List<ByHoursWeatherDbModel>)

    @Query("SELECT * FROM weather_by_hours")
    fun loadByHoursWeather(): LiveData<List<ByHoursWeatherDbModel>>

}