package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.DailyForecastUiModel
import com.dendron.easyweather.presentation.home.WeatherPalette
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@Composable
fun DailyForecastSection(
    items: List<DailyForecastUiModel>,
    palette: WeatherPalette,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WeatherDimens.screenPadding),
    ) {
        Text(
            text = stringResource(R.string.weather_daily_title),
            color = palette.primaryText,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = WeatherDimens.mediumSpacing),
        )

        Surface(
            color = palette.cardTint.copy(alpha = 0.16f),
            shape = RoundedCornerShape(WeatherDimens.cardCornerLarge),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                items.forEach { item ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                    ) {
                        Text(
                            text = item.dayLabel,
                            color = palette.secondaryText,
                            style = MaterialTheme.typography.body1,
                        )
                        Text(
                            text = item.rangeText,
                            color = palette.primaryText,
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }
        }
    }
}
