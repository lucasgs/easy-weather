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
    val humidityPercent: Int,
    val precipitationChancePercent: Int,
    val sunrise: String,
    val sunset: String,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>,
)
