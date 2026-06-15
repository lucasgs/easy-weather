package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.domain.Weather
import javax.inject.Inject

class WeatherUiModelMapper @Inject constructor(
    private val weatherDescriptionMapper: WeatherDescriptionMapper,
    private val weatherIconMapper: WeatherIconMapper,
    private val windDirectionMapper: WindDirectionMapper,
) {
    fun map(weather: Weather): WeatherUiModel {
        val windDirectionText = windDirectionMapper.map(weather.windDirection)
        return WeatherUiModel(
            weatherIcon = weatherIconMapper.map(weather.weatherCode),
            descriptionText = weatherDescriptionMapper.map(weather.weatherCode),
            currentTemperatureText = "${weather.currentTemperature}°",
            highLowText = "H:${weather.maxTemperature}°  L:${weather.minTemperature}°",
            windText = "${weather.windSpeed} ${weather.weatherUnit.wind} $windDirectionText",
            temperatureText = "${weather.minTemperature}° / ${weather.currentTemperature}° / ${weather.maxTemperature}°",
        )
    }
}
