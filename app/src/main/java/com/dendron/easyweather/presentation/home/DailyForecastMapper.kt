package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.domain.DailyForecast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dailySourceFormatter = DateTimeFormatter.ISO_LOCAL_DATE
private val dailyTargetFormatter = DateTimeFormatter.ofPattern("EEE", Locale.US)

fun DailyForecast.toUiModel(): DailyForecastUiModel {
    val parsedDate = runCatching { LocalDate.parse(date, dailySourceFormatter) }.getOrNull()
    val dayLabel = parsedDate?.format(dailyTargetFormatter) ?: date
    return DailyForecastUiModel(
        dayLabel = dayLabel,
        rangeText = "${minTemperature}° / ${maxTemperature}°",
    )
}
