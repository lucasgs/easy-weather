package com.dendron.easyweather.presentation.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.dendron.easyweather.presentation.ui.theme.WhiteLight

@Immutable
data class WeatherPalette(
    val backgroundTop: Color,
    val backgroundBottom: Color,
    val accent: Color,
    val cardTint: Color,
    val primaryText: Color = WhiteLight,
    val secondaryText: Color = Color(0xFFE7F0FA),
)

fun weatherPaletteForCode(weatherCode: Int): WeatherPalette = when (weatherCode) {
    0 -> WeatherPalette(
        backgroundTop = Color(0xFF0F4C81),
        backgroundBottom = Color(0xFF3D86C2),
        accent = Color(0xFFFFD166),
        cardTint = Color(0xFFB7E1FF),
    )

    1, 2, 3 -> WeatherPalette(
        backgroundTop = Color(0xFF32506E),
        backgroundBottom = Color(0xFF647C94),
        accent = Color(0xFFF6C177),
        cardTint = Color(0xFFD7E3F0),
    )

    45, 48 -> WeatherPalette(
        backgroundTop = Color(0xFF415161),
        backgroundBottom = Color(0xFF637380),
        accent = Color(0xFFAED9E0),
        cardTint = Color(0xFFDCE6EB),
    )

    51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> WeatherPalette(
        backgroundTop = Color(0xFF17324A),
        backgroundBottom = Color(0xFF2F5E85),
        accent = Color(0xFF7FDBFF),
        cardTint = Color(0xFFC5E4FF),
    )

    71, 73, 75, 77, 85, 86 -> WeatherPalette(
        backgroundTop = Color(0xFF35566E),
        backgroundBottom = Color(0xFF6F96B3),
        accent = Color(0xFFEAF7FF),
        cardTint = Color(0xFFE2F1FB),
    )

    95, 96, 99 -> WeatherPalette(
        backgroundTop = Color(0xFF22233E),
        backgroundBottom = Color(0xFF4C4D73),
        accent = Color(0xFFFFC857),
        cardTint = Color(0xFFD7D8F7),
    )

    else -> WeatherPalette(
        backgroundTop = Color(0xFF1C3879),
        backgroundBottom = Color(0xFF496894),
        accent = Color(0xFFF9F5EB),
        cardTint = Color(0xFFEAE3D2),
    )
}

val DefaultWeatherPalette = weatherPaletteForCode(-1)
