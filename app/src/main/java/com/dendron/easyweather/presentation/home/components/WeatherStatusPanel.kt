package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.dendron.easyweather.presentation.home.WeatherPalette
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@Composable
fun WeatherStatusPanel(
    palette: WeatherPalette,
    title: String,
    message: String,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
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
                text = title,
                color = palette.primaryText,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
            )
            Text(
                text = message,
                color = palette.secondaryText,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = WeatherDimens.mediumSpacing),
            )

            if (primaryActionLabel != null && onPrimaryAction != null) {
                Button(
                    onClick = onPrimaryAction,
                    modifier = Modifier.padding(top = WeatherDimens.sectionSpacing),
                ) {
                    Text(text = primaryActionLabel)
                }
            }

            if (secondaryActionLabel != null && onSecondaryAction != null) {
                OutlinedButton(
                    onClick = onSecondaryAction,
                    modifier = Modifier.padding(top = WeatherDimens.mediumSpacing),
                ) {
                    Text(text = secondaryActionLabel)
                }
            }
        }
    }
}
