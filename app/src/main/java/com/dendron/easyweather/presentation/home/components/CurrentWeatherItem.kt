package com.dendron.easyweather.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.presentation.ui.theme.BlueLight

@Composable
fun CurrentWeatherItem(
    @DrawableRes iconId: Int,
    iconDescription: String,
    text: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp),
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = iconDescription,
            colorFilter = ColorFilter.tint(BlueLight),
        )
        Text(text = text, color = Color.White)
    }
}
