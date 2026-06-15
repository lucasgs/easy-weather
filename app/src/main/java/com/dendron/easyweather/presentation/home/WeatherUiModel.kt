package com.dendron.easyweather.presentation.home

data class WeatherUiModel(
    val locationName: String,
    val descriptionText: String,
    val currentTemperatureText: String,
    val highLowText: String,
    val windText: String,
    val temperatureText: String,
    val humidityText: String,
    val precipitationText: String,
    val sunriseText: String,
    val sunsetText: String,
    val hourlyForecast: List<HourlyForecastUiModel>,
    val dailyForecast: List<DailyForecastUiModel>,
    val weatherIcon: Int,
    val palette: WeatherPalette,
)
