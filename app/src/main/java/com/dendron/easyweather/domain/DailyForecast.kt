package com.dendron.easyweather.domain

data class DailyForecast(
    val date: String,
    val minTemperature: Double,
    val maxTemperature: Double,
)
