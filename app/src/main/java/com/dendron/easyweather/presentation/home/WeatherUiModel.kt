package com.dendron.easyweather.presentation.home

data class WeatherUiModel(
    val descriptionText: String,
    val currentTemperatureText: String,
    val highLowText: String,
    val windText: String,
    val temperatureText: String,
    val weatherIcon: Int,
)
