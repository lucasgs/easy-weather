package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.R
import com.dendron.easyweather.domain.HourlyForecast
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherUnits
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherUiModelMapperTest {

    private val mapper = WeatherUiModelMapper(
        weatherDescriptionMapper = WeatherDescriptionMapper(),
        weatherIconMapper = WeatherIconMapper(),
        windDirectionMapper = WindDirectionMapper(),
    )

    @Test
    fun `map should create ui model from weather`() {
        val result = mapper.map(fakeWeather)

        assertEquals(
            WeatherUiModel(
                locationName = "New York",
                descriptionText = "Mainly clear, partly cloudy, and overcast",
                currentTemperatureText = "10.0°",
                highLowText = "H:1.0°  L:2.0°",
                windText = "50.0 km/h SE",
                temperatureText = "2.0° / 10.0° / 1.0°",
                hourlyForecast = listOf(
                    HourlyForecastUiModel(timeText = "1pm", temperatureText = "11.0°"),
                    HourlyForecastUiModel(timeText = "2pm", temperatureText = "12.5°"),
                ),
                weatherIcon = R.drawable.cloudy,
            ),
            result,
        )
    }

    private companion object {
        val fakeWeather = Weather(
            locationName = "New York",
            weatherUnit = WeatherUnits(
                temperature = "",
                wind = "km/h",
            ),
            currentTemperature = 10.0,
            maxTemperature = 1.0,
            minTemperature = 2.0,
            windSpeed = 50.0,
            windDirection = 120,
            weatherCode = 1,
            hourlyForecast = listOf(
                HourlyForecast(time = "2026-06-14T13:00", temperature = 11.0),
                HourlyForecast(time = "2026-06-14T14:00", temperature = 12.5),
            ),
        )
    }
}
