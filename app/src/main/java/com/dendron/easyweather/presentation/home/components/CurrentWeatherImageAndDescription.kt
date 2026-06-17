package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.WeatherUiModel
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens
import java.text.DateFormat
import java.util.Date

@Composable
fun CurrentWeatherImageAndDescription(
    model: WeatherUiModel,
    lastUpdatedAtMillis: Long,
    onChangeCity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lastUpdatedText = remember(lastUpdatedAtMillis) {
        DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(lastUpdatedAtMillis))
    }

    Surface(
        color = model.palette.cardTint.copy(alpha = 0.16f),
        shape = RoundedCornerShape(WeatherDimens.cardCornerLarge),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WeatherDimens.screenPadding),
    ) {
        Column(modifier = Modifier.padding(WeatherDimens.contentPadding)) {
            Text(
                text = model.locationName,
                color = model.palette.secondaryText,
                style = MaterialTheme.typography.overline,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.weather_today_title),
                color = model.palette.secondaryText,
                style = MaterialTheme.typography.caption,
            )
            Spacer(modifier = Modifier.height(WeatherDimens.mediumSpacing))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WeatherDimens.largeSpacing),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = model.weatherIcon),
                    contentDescription = model.descriptionText,
                    colorFilter = ColorFilter.tint(model.palette.accent),
                    modifier = Modifier
                        .size(88.dp)
                        .background(
                            color = model.palette.primaryText.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(WeatherDimens.cardCornerSmall),
                        )
                        .padding(18.dp),
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.currentTemperatureText,
                        color = model.palette.primaryText,
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = model.descriptionText,
                        color = model.palette.primaryText,
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = model.highLowText,
                        color = model.palette.secondaryText,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
            Spacer(modifier = Modifier.height(WeatherDimens.largeSpacing))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.weather_last_updated, lastUpdatedText),
                    color = model.palette.secondaryText,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(onClick = onChangeCity) {
                    Text(text = stringResource(R.string.weather_change_city_action))
                }
            }
        }
    }
}
