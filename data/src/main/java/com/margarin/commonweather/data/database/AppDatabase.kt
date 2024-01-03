package com.margarin.commonweather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.margarin.commonweather.data.database.dao.SearchDao
import com.margarin.commonweather.data.database.dao.WeatherDao
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.CurrentWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.SearchDbModel

@Database(
    entities = [
        CurrentWeatherDbModel::class,
        ByHoursWeatherDbModel::class,
        ByDaysWeatherDbModel::class,
        SearchDbModel::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {

        private var db: AppDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun weatherDao(): WeatherDao
    abstract fun searchDao(): SearchDao

}