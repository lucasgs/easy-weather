package com.dendron.easyweather.domain

data class Weather(
    val locationName: String,
    val weatherUnit: WeatherUnits,
    val currentTemperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val weatherCode: Int,
)
