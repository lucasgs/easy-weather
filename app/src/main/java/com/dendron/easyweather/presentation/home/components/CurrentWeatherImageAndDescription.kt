package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.WeatherUiModel
import com.dendron.easyweather.presentation.ui.theme.BlueLight
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

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(50.dp),
    ) {
        Image(
            painter = painterResource(id = model.weatherIcon),
            contentDescription = model.descriptionText,
            colorFilter = ColorFilter.tint(BlueLight),
            modifier = Modifier.size(60.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = model.descriptionText,
            color = WhiteLight,
            fontSize = MaterialTheme.typography.h5.fontSize,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.weather_last_updated, lastUpdatedText),
            color = WhiteLight,
            style = MaterialTheme.typography.body2,
        )
    }
}
