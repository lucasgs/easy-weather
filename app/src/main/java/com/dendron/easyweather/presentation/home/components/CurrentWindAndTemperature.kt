package com.dendron.easyweather.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.WeatherUiModel
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

private data class WeatherConditionItemUi(
    val title: String,
    @DrawableRes val iconId: Int,
    val iconDescription: String,
    val text: String,
)

@Composable
fun CurrentWeatherConditions(
    model: WeatherUiModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WeatherDimens.screenPadding),
    ) {
        Text(
            text = stringResource(R.string.weather_condition_title),
            color = model.palette.primaryText,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = WeatherDimens.mediumSpacing),
        )
        val items = listOf(
            WeatherConditionItemUi(
                title = stringResource(R.string.weather_wind_label),
                iconId = R.drawable.wind,
                iconDescription = stringResource(R.string.weather_wind_icon_description),
                text = model.windText,
            ),
            WeatherConditionItemUi(
                title = stringResource(R.string.weather_temperature_range_label),
                iconId = R.drawable.thermometer,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.temperatureText,
            ),
            WeatherConditionItemUi(
                title = stringResource(R.string.weather_humidity_label),
                iconId = R.drawable.thermometer,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.humidityText,
            ),
            WeatherConditionItemUi(
                title = stringResource(R.string.weather_precipitation_label),
                iconId = R.drawable.rain,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.precipitationText,
            ),
            WeatherConditionItemUi(
                title = stringResource(R.string.weather_sunrise_label),
                iconId = R.drawable.day_sunny,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.sunriseText,
            ),
            WeatherConditionItemUi(
                title = stringResource(R.string.weather_sunset_label),
                iconId = R.drawable.day_sunny,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.sunsetText,
            ),
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val singleColumn = maxWidth < 360.dp || LocalDensity.current.fontScale >= 1.25f
            val rows = if (singleColumn) items.map { listOf(it) } else items.chunked(2)

            Column(verticalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing)) {
                rows.forEach { rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(androidx.compose.foundation.layout.IntrinsicSize.Min),
                    ) {
                        rowItems.forEach { item ->
                            CurrentWeatherItem(
                                title = item.title,
                                iconId = item.iconId,
                                iconDescription = item.iconDescription,
                                text = item.text,
                                accentColor = model.palette.accent,
                                surfaceTint = model.palette.cardTint,
                                primaryTextColor = model.palette.primaryText,
                                secondaryTextColor = model.palette.secondaryText,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (rowItems.size == 1 && !singleColumn) {
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
