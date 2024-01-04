package com.margarin.commonweather.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.margarin.commonweather.dbmodels.SearchDbModel

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchItem(search: SearchDbModel)

    @Query("SELECT * FROM search_table")
    fun getSearchList(): LiveData<List<SearchDbModel>>

    @Query("DELETE FROM search_table WHERE id=:searchId")
    suspend fun deleteSearchItem(searchId: Int)

    @Query("SELECT * FROM search_table WHERE id=:searchId LIMIT 1")
    suspend fun getSearchItem(searchId: Int): SearchDbModel


}