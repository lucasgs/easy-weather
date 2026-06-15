package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.R
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
                descriptionText = "Mainly clear, partly cloudy, and overcast",
                currentTemperatureText = "10.0°",
                highLowText = "H:1.0°  L:2.0°",
                windText = "50.0 km/h SE",
                temperatureText = "2.0° / 10.0° / 1.0°",
                weatherIcon = R.drawable.cloudy,
            ),
            result,
        )
    }

    private companion object {
        val fakeWeather = Weather(
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
        )
    }
}
