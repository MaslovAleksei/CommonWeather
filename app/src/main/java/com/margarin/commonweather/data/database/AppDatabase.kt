package com.margarin.commonweather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.margarin.commonweather.data.database.dao.ByDaysWeatherDao
import com.margarin.commonweather.data.database.dao.ByHoursWeatherDao
import com.margarin.commonweather.data.database.dao.CurrentWeatherDao
import com.margarin.commonweather.data.database.dbmodels.ByDaysWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.ByHoursWeatherDbModel
import com.margarin.commonweather.data.database.dbmodels.CurrentWeatherDbModel

@Database(
    entities = [
        CurrentWeatherDbModel::class,
        ByHoursWeatherDbModel::class,
        ByDaysWeatherDbModel::class
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

    abstract fun byDaysWeatherDao(): ByDaysWeatherDao
    abstract fun byHoursWeatherDao(): ByHoursWeatherDao
    abstract fun currentWeatherDao(): CurrentWeatherDao

}