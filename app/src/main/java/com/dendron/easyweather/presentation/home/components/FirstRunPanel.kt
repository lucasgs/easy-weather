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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.ui.theme.BlueLight
import com.dendron.easyweather.presentation.ui.theme.WhiteDark
import com.dendron.easyweather.presentation.ui.theme.WhiteLight

@Composable
fun FirstRunPanel(
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = WhiteLight.copy(alpha = 0.08f),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.weather_empty_title),
                color = WhiteLight,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.weather_empty_message),
                color = WhiteDark,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(28.dp))
            BenefitRow(text = stringResource(R.string.weather_empty_benefit_current))
            BenefitRow(text = stringResource(R.string.weather_empty_benefit_refresh))
            BenefitRow(text = stringResource(R.string.weather_empty_benefit_privacy))
            Spacer(modifier = Modifier.height(28.dp))
            Button(onClick = onPrimaryAction) {
                Text(text = stringResource(R.string.weather_get_started_action))
            }
        }
    }
}

@Composable
private fun BenefitRow(text: String) {
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
                .background(BlueLight, CircleShape),
        )
        Text(
            text = text,
            color = WhiteLight,
            style = MaterialTheme.typography.body1,
        )
    }
}
