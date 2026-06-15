package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.R
import com.dendron.easyweather.domain.DailyForecast
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
                humidityText = "65%",
                precipitationText = "40%",
                sunriseText = "6:30 am",
                sunsetText = "8:15 pm",
                hourlyForecast = listOf(
                    HourlyForecastUiModel(timeText = "1pm", temperatureText = "11.0°"),
                    HourlyForecastUiModel(timeText = "2pm", temperatureText = "12.5°"),
                ),
                dailyForecast = listOf(
                    DailyForecastUiModel(dayLabel = "Sun", rangeText = "2.0° / 8.0°"),
                    DailyForecastUiModel(dayLabel = "Mon", rangeText = "3.0° / 9.0°"),
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
            humidityPercent = 65,
            precipitationChancePercent = 40,
            sunrise = "2026-06-14T06:30",
            sunset = "2026-06-14T20:15",
            hourlyForecast = listOf(
                HourlyForecast(time = "2026-06-14T13:00", temperature = 11.0),
                HourlyForecast(time = "2026-06-14T14:00", temperature = 12.5),
            ),
            dailyForecast = listOf(
                DailyForecast(date = "2026-06-14", minTemperature = 2.0, maxTemperature = 8.0),
                DailyForecast(date = "2026-06-15", minTemperature = 3.0, maxTemperature = 9.0),
            ),
        )
    }
}
