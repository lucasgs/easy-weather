package com.dendron.easyweather.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.presentation.ui.theme.BlueLight
import com.dendron.easyweather.presentation.ui.theme.WhiteDark
import com.dendron.easyweather.presentation.ui.theme.WhiteLight

@Composable
fun CurrentWeatherItem(
    title: String,
    @DrawableRes iconId: Int,
    iconDescription: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = WhiteLight.copy(alpha = 0.12f),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxHeight(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = title,
                color = WhiteDark,
                style = MaterialTheme.typography.overline,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = iconDescription,
                    colorFilter = ColorFilter.tint(BlueLight),
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = text,
                    color = WhiteLight,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}
