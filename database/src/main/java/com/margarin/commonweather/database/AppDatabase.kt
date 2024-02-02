package com.margarin.commonweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.margarin.commonweather.dao.SearchDao
import com.margarin.commonweather.dao.WeatherDao
import com.margarin.commonweather.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.dbmodels.CityDbModel
import com.margarin.commonweather.dbmodels.CurrentWeatherDbModel

@Database(
    entities = [
        CurrentWeatherDbModel::class,
        ByHoursWeatherDbModel::class,
        ByDaysWeatherDbModel::class,
        CityDbModel::class
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