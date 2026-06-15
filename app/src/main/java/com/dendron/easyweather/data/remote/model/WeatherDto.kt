package com.dendron.easyweather.data.remote.model


import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherUnits
import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,
    @SerializedName("daily")
    val daily: Daily,
    @SerializedName("daily_units")
    val dailyUnits: DailyUnits,
    @SerializedName("elevation")
    val elevation: Int,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,
    @SerializedName("hourly")
    val hourly: Hourly,
    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int
)

fun WeatherDto.toDomain() = Weather(
    locationName = timezone.substringAfterLast('/').replace('_', ' '),
    currentTemperature = currentWeather.temperature,
    maxTemperature = daily.temperature2mMax.max(),
    minTemperature = daily.temperature2mMin.min(),
    windSpeed = currentWeather.windspeed,
    windDirection = currentWeather.winddirection,
    weatherCode = currentWeather.weathercode,
    weatherUnit = WeatherUnits(
        temperature = dailyUnits.temperature2mMax,
        wind = dailyUnits.windspeed10mMax,
    ),
)