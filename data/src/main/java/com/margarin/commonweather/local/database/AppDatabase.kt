package com.margarin.commonweather.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.margarin.commonweather.local.dao.SearchDao
import com.margarin.commonweather.local.dao.WeatherDao
import com.margarin.commonweather.local.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.local.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.local.dbmodels.CityDbModel
import com.margarin.commonweather.local.dbmodels.CurrentWeatherDbModel

@Database(
    entities = [
        CurrentWeatherDbModel::class,
        ByHoursWeatherDbModel::class,
        ByDaysWeatherDbModel::class,
        CityDbModel::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun searchDao(): SearchDao

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
}