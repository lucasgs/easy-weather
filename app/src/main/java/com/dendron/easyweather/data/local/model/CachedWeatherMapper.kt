package com.dendron.easyweather.data.local.model

import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherUnits

fun Weather.toCachedEntity(
    latitude: Double,
    longitude: Double,
    cachedAtMillis: Long,
): CachedWeatherEntity = CachedWeatherEntity(
    latitude = latitude,
    longitude = longitude,
    locationName = locationName,
    temperatureUnit = weatherUnit.temperature,
    windUnit = weatherUnit.wind,
    currentTemperature = currentTemperature,
    maxTemperature = maxTemperature,
    minTemperature = minTemperature,
    windSpeed = windSpeed,
    windDirection = windDirection,
    weatherCode = weatherCode,
    humidityPercent = humidityPercent,
    precipitationChancePercent = precipitationChancePercent,
    sunrise = sunrise,
    sunset = sunset,
    hourlyForecast = hourlyForecast,
    dailyForecast = dailyForecast,
    cachedAtMillis = cachedAtMillis,
)

fun CachedWeatherEntity.toDomain(): Weather = Weather(
    locationName = locationName,
    weatherUnit = WeatherUnits(
        temperature = temperatureUnit,
        wind = windUnit,
    ),
    currentTemperature = currentTemperature,
    maxTemperature = maxTemperature,
    minTemperature = minTemperature,
    windSpeed = windSpeed,
    windDirection = windDirection,
    weatherCode = weatherCode,
    humidityPercent = humidityPercent,
    precipitationChancePercent = precipitationChancePercent,
    sunrise = sunrise,
    sunset = sunset,
    hourlyForecast = hourlyForecast,
    dailyForecast = dailyForecast,
)
