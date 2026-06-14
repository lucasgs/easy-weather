package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.R
import javax.inject.Inject

class WeatherIconMapper @Inject constructor() {
    fun map(weatherCode: Int): Int = when (weatherCode) {
        0 -> R.drawable.day_sunny
        1, 2, 3 -> R.drawable.cloudy
        45, 48 -> R.drawable.fog
        51, 53, 55, 56, 57 -> R.drawable.drizzle
        61, 63, 65, 66, 67 -> R.drawable.rain
        71, 73, 75, 77 -> R.drawable.snow
        80, 81, 82, 85, 86 -> R.drawable.showers
        95, 96, 99 -> R.drawable.thunderstorm
        else -> R.drawable.na
    }
}
