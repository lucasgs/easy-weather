package com.dendron.easyweather.data.local

import androidx.room.TypeConverter
import com.dendron.easyweather.domain.DailyForecast
import com.dendron.easyweather.domain.HourlyForecast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromHourlyForecastList(value: List<HourlyForecast>): String = gson.toJson(value)

    @TypeConverter
    fun toHourlyForecastList(value: String): List<HourlyForecast> {
        val type = object : TypeToken<List<HourlyForecast>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromDailyForecastList(value: List<DailyForecast>): String = gson.toJson(value)

    @TypeConverter
    fun toDailyForecastList(value: String): List<DailyForecast> {
        val type = object : TypeToken<List<DailyForecast>>() {}.type
        return gson.fromJson(value, type)
    }
}
