package com.margarin.commonweather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.dbmodels.CityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchItem(search: CityDbModel)

    @Query("SELECT * FROM search_table")
    fun getSavedCityList(): Flow<List<CityDbModel>>

    @Query("DELETE FROM search_table WHERE id=:searchId")
    suspend fun removeFromFavourites(searchId: Int)

    @Query("SELECT * FROM search_table WHERE id=:searchId LIMIT 1")
    suspend fun getSearchItem(searchId: Int): CityDbModel


}