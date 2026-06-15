package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.WeatherPalette
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@Composable
fun FirstRunPanel(
    palette: WeatherPalette,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = palette.cardTint.copy(alpha = 0.12f),
        shape = RoundedCornerShape(WeatherDimens.cardCornerLarge),
        modifier = modifier
            .fillMaxSize()
            .padding(WeatherDimens.screenPadding),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(WeatherDimens.contentPadding),
        ) {
            Text(
                text = stringResource(R.string.weather_empty_title),
                color = palette.primaryText,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(WeatherDimens.mediumSpacing))
            Text(
                text = stringResource(R.string.weather_empty_message),
                color = palette.secondaryText,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(28.dp))
            BenefitRow(text = stringResource(R.string.weather_empty_benefit_current), palette = palette)
            BenefitRow(text = stringResource(R.string.weather_empty_benefit_refresh), palette = palette)
            BenefitRow(text = stringResource(R.string.weather_empty_benefit_privacy), palette = palette)
            Spacer(modifier = Modifier.height(28.dp))
            Button(onClick = onPrimaryAction) {
                Text(text = stringResource(R.string.weather_get_started_action))
            }
            Spacer(modifier = Modifier.height(WeatherDimens.mediumSpacing))
            OutlinedButton(onClick = onSecondaryAction) {
                Text(text = stringResource(R.string.weather_choose_city_action))
            }
        }
    }
}

@Composable
private fun BenefitRow(
    text: String,
    palette: WeatherPalette,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(palette.accent, CircleShape),
        )
        Text(
            text = text,
            color = palette.primaryText,
            style = MaterialTheme.typography.body1,
        )
    }
}
