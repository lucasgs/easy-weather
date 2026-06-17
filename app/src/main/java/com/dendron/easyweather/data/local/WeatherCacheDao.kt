package com.dendron.easyweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dendron.easyweather.data.local.model.CachedWeatherEntity

@Dao
interface WeatherCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: CachedWeatherEntity)

    @Query("SELECT * FROM cached_weather WHERE latitude = :latitude AND longitude = :longitude LIMIT 1")
    suspend fun getByCoordinates(latitude: Double, longitude: Double): CachedWeatherEntity?
}
