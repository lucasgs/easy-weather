package com.dendron.easyweather.data.remote

import com.dendron.easyweather.data.remote.model.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast?hourly=temperature_2m,relative_humidity_2m,precipitation_probability&daily=temperature_2m_max,temperature_2m_min,windspeed_10m_max,sunrise,sunset&current_weather=true&past_days=1&forecast_days=5&timezone=auto")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: String,
        @Query("windspeed_unit") windSpeedUnit: String,
    ): WeatherDto
}