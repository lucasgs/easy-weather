package com.dendron.easyweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dendron.easyweather.data.local.model.CachedWeatherEntity

@Database(
    entities = [CachedWeatherEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherCacheDao(): WeatherCacheDao
}
