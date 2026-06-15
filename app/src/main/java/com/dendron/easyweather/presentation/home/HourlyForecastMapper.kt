package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.domain.HourlyForecast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val sourceFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
private val targetFormatter = DateTimeFormatter.ofPattern("ha", Locale.US)

fun HourlyForecast.toUiModel(): HourlyForecastUiModel {
    val parsedTime = runCatching { LocalDateTime.parse(time, sourceFormatter) }.getOrNull()
    val timeText = parsedTime?.format(targetFormatter)?.lowercase(Locale.US) ?: time
    return HourlyForecastUiModel(
        timeText = timeText,
        temperatureText = "${temperature}°",
    )
}
