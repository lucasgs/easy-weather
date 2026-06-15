package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.WeatherUiModel
import com.dendron.easyweather.presentation.ui.theme.WhiteLight

@Composable
fun CurrentWeatherConditions(
    model: WeatherUiModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.weather_condition_title),
            color = WhiteLight,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            CurrentWeatherItem(
                title = stringResource(R.string.weather_wind_label),
                iconId = R.drawable.wind,
                iconDescription = stringResource(R.string.weather_wind_icon_description),
                text = model.windText,
                modifier = Modifier.weight(1f),
            )
            CurrentWeatherItem(
                title = stringResource(R.string.weather_temperature_range_label),
                iconId = R.drawable.thermometer,
                iconDescription = stringResource(R.string.weather_temperature_icon_description),
                text = model.temperatureText,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
