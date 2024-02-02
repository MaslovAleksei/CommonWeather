package com.margarin.commonweather.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.local.dbmodels.CityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Query("SELECT * FROM search_table")
    fun getFavouriteCities(): Flow<List<CityDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(cityDbModel: CityDbModel)

    @Query("DELETE FROM search_table WHERE id=:cityId")
    suspend fun removeFromFavourites(cityId: Int)

    @Query("SELECT EXISTS (SELECT * FROM search_table WHERE id=:cityId LIMIT 1)")
    fun observeIsFavourite(cityId: Int) : Flow<Boolean>

}