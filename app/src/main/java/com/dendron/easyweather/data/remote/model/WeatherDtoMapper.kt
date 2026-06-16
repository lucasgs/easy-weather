package com.dendron.easyweather.data.remote.model

import com.dendron.easyweather.domain.DailyForecast
import com.dendron.easyweather.domain.HourlyForecast
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherUnits

fun WeatherDto.toDomain(): Weather {
    val currentIndex = hourly.time.indexOf(currentWeather.time).takeIf { it >= 0 } ?: 0
    return Weather(
        locationName = timezone.substringAfterLast('/').replace('_', ' '),
        currentTemperature = currentWeather.temperature,
        maxTemperature = daily.temperature2mMax.max(),
        minTemperature = daily.temperature2mMin.min(),
        windSpeed = currentWeather.windspeed,
        windDirection = currentWeather.winddirection,
        weatherCode = currentWeather.weathercode,
        humidityPercent = hourly.relativeHumidity2m.getOrElse(currentIndex) { hourly.relativeHumidity2m.firstOrNull() ?: 0 },
        precipitationChancePercent = hourly.precipitationProbability.getOrElse(currentIndex) { hourly.precipitationProbability.firstOrNull() ?: 0 },
        sunrise = daily.sunrise.firstOrNull().orEmpty(),
        sunset = daily.sunset.firstOrNull().orEmpty(),
        hourlyForecast = hourly.time.zip(hourly.temperature2m)
            .take(8)
            .map { (time, temperature) ->
                HourlyForecast(
                    time = time,
                    temperature = temperature,
                )
            },
        dailyForecast = daily.time.zip(daily.temperature2mMin.zip(daily.temperature2mMax))
            .take(5)
            .map { (date, temps) ->
                DailyForecast(
                    date = date,
                    minTemperature = temps.first,
                    maxTemperature = temps.second,
                )
            },
        weatherUnit = WeatherUnits(
            temperature = dailyUnits.temperature2mMax,
            wind = dailyUnits.windspeed10mMax,
        ),
    )
}
