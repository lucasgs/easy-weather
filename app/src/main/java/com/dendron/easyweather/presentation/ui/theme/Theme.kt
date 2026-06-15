package com.dendron.easyweather.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.dendron.easyweather.ui.theme.Shapes
import com.dendron.easyweather.ui.theme.Typography

private val DarkColorPalette = darkColors(
    primary = WhiteLight,
    primaryVariant = BlueLight,
    secondary = Red200,
    background = Navy,
    surface = BlueLight,
    onPrimary = Navy,
    onBackground = WhiteLight,
    onSurface = WhiteLight,
)

private val LightColorPalette = lightColors(
    primary = Navy,
    primaryVariant = BlueLight,
    secondary = Red500,
    background = WhiteLight,
    surface = WhiteDark,
    onPrimary = WhiteLight,
    onBackground = Navy,
    onSurface = Navy,
)

@Composable
fun EasyWeatherTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
