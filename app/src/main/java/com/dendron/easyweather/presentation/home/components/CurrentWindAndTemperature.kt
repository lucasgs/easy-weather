package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.WeatherUiModel
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

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
        Row(
            horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            CurrentWeatherItem(
                title = stringResource(R.string.weather_wind_label),
                iconId = R.drawable.wind,
                iconDescription = stringResource(R.string.weather_wind_icon_description),
                text = model.windText,
                accentColor = model.palette.accent,
                surfaceTint = model.palette.cardTint,
                primaryTextColor = model.palette.primaryText,
                secondaryTextColor = model.palette.secondaryText,
                modifier = Modifier.weight(1f),
            )
            CurrentWeatherItem(
                title = stringResource(R.string.weather_temperature_range_label),
                iconId = R.drawable.thermometer,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.temperatureText,
                accentColor = model.palette.accent,
                surfaceTint = model.palette.cardTint,
                primaryTextColor = model.palette.primaryText,
                secondaryTextColor = model.palette.secondaryText,
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(top = WeatherDimens.mediumSpacing),
        ) {
            CurrentWeatherItem(
                title = stringResource(R.string.weather_humidity_label),
                iconId = R.drawable.thermometer,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.humidityText,
                accentColor = model.palette.accent,
                surfaceTint = model.palette.cardTint,
                primaryTextColor = model.palette.primaryText,
                secondaryTextColor = model.palette.secondaryText,
                modifier = Modifier.weight(1f),
            )
            CurrentWeatherItem(
                title = stringResource(R.string.weather_precipitation_label),
                iconId = R.drawable.rain,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.precipitationText,
                accentColor = model.palette.accent,
                surfaceTint = model.palette.cardTint,
                primaryTextColor = model.palette.primaryText,
                secondaryTextColor = model.palette.secondaryText,
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(top = WeatherDimens.mediumSpacing),
        ) {
            CurrentWeatherItem(
                title = stringResource(R.string.weather_sunrise_label),
                iconId = R.drawable.day_sunny,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.sunriseText,
                accentColor = model.palette.accent,
                surfaceTint = model.palette.cardTint,
                primaryTextColor = model.palette.primaryText,
                secondaryTextColor = model.palette.secondaryText,
                modifier = Modifier.weight(1f),
            )
            CurrentWeatherItem(
                title = stringResource(R.string.weather_sunset_label),
                iconId = R.drawable.day_sunny,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.sunsetText,
                accentColor = model.palette.accent,
                surfaceTint = model.palette.cardTint,
                primaryTextColor = model.palette.primaryText,
                secondaryTextColor = model.palette.secondaryText,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
