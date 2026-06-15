package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.HourlyForecastUiModel
import com.dendron.easyweather.presentation.home.WeatherPalette
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@Composable
fun HourlyForecastSection(
    items: List<HourlyForecastUiModel>,
    palette: WeatherPalette,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = WeatherDimens.screenPadding),
    ) {
        Text(
            text = stringResource(R.string.weather_hourly_title),
            color = palette.primaryText,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = WeatherDimens.mediumSpacing),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing),
            contentPadding = PaddingValues(end = WeatherDimens.screenPadding),
        ) {
            items(items) { item ->
                Surface(
                    color = palette.cardTint.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(WeatherDimens.cardCornerMedium),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                    ) {
                        Text(
                            text = item.timeText,
                            color = palette.secondaryText,
                            style = MaterialTheme.typography.overline,
                        )
                        Text(
                            text = item.temperatureText,
                            color = palette.primaryText,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
