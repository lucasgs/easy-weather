package com.dendron.easyweather.data.local.model

import androidx.room.Entity
import com.dendron.easyweather.domain.DailyForecast
import com.dendron.easyweather.domain.HourlyForecast

@Entity(
    tableName = "cached_weather",
    primaryKeys = ["latitude", "longitude"],
)
data class CachedWeatherEntity(
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val temperatureUnit: String,
    val windUnit: String,
    val currentTemperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val weatherCode: Int,
    val humidityPercent: Int,
    val precipitationChancePercent: Int,
    val sunrise: String,
    val sunset: String,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>,
    val cachedAtMillis: Long,
)
