package com.dendron.easyweather.presentation.home

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dateTimeSourceFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
private val timeTargetFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

fun String.toDisplayTime(): String {
    val parsed = runCatching { LocalDateTime.parse(this, dateTimeSourceFormatter) }.getOrNull()
    return parsed?.format(timeTargetFormatter)?.lowercase(Locale.US) ?: this
}
