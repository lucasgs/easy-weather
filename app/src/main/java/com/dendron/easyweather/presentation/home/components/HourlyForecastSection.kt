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
import com.dendron.easyweather.presentation.ui.theme.WhiteDark
import com.dendron.easyweather.presentation.ui.theme.WhiteLight

@Composable
fun HourlyForecastSection(
    items: List<HourlyForecastUiModel>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.weather_hourly_title),
            color = WhiteLight,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 20.dp),
        ) {
            items(items) { item ->
                Surface(
                    color = WhiteLight.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                    ) {
                        Text(
                            text = item.timeText,
                            color = WhiteDark,
                            style = MaterialTheme.typography.overline,
                        )
                        Text(
                            text = item.temperatureText,
                            color = WhiteLight,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
