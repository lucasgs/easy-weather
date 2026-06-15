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
import com.dendron.easyweather.presentation.ui.theme.BlueLight
import com.dendron.easyweather.presentation.ui.theme.WhiteDark
import com.dendron.easyweather.presentation.ui.theme.WhiteLight
import java.text.DateFormat
import java.util.Date

@Composable
fun CurrentWeatherImageAndDescription(
    model: WeatherUiModel,
    lastUpdatedAtMillis: Long,
) {
    val lastUpdatedText = remember(lastUpdatedAtMillis) {
        DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(lastUpdatedAtMillis))
    }

    Surface(
        color = WhiteLight.copy(alpha = 0.12f),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = model.locationName,
                color = WhiteDark,
                style = MaterialTheme.typography.overline,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.weather_today_title),
                color = WhiteDark,
                style = MaterialTheme.typography.caption,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = model.weatherIcon),
                    contentDescription = model.descriptionText,
                    colorFilter = ColorFilter.tint(BlueLight),
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = WhiteLight.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(18.dp),
                        )
                        .padding(14.dp),
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.currentTemperatureText,
                        color = WhiteLight,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = model.descriptionText,
                        color = WhiteLight,
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = model.highLowText,
                        color = WhiteDark,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.weather_last_updated, lastUpdatedText),
                color = WhiteDark,
                style = MaterialTheme.typography.body2,
            )
        }
    }
}
