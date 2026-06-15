package com.dendron.easyweather.presentation.home.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.presentation.home.WeatherPalette
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@Composable
fun LoadingWeatherSkeleton(
    palette: WeatherPalette,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(WeatherDimens.sectionSpacing),
        contentPadding = PaddingValues(vertical = WeatherDimens.screenPadding),
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            SkeletonCard(
                palette = palette,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WeatherDimens.screenPadding)
                    .height(220.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SkeletonLine(palette, width = 110.dp)
                    SkeletonLine(palette, width = 70.dp, height = 12.dp)
                    Box(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SkeletonLine(palette, width = 84.dp, height = 84.dp)
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            SkeletonLine(palette, width = 120.dp, height = 36.dp)
                            SkeletonLine(palette, width = 160.dp)
                            SkeletonLine(palette, width = 96.dp)
                        }
                    }
                    Box(modifier = Modifier.height(8.dp))
                    SkeletonLine(palette, width = 140.dp)
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WeatherDimens.screenPadding),
            ) {
                SkeletonLine(palette, width = 136.dp, height = 18.dp)
                Box(modifier = Modifier.height(14.dp))
                repeat(3) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SkeletonCard(
                            palette = palette,
                            modifier = Modifier
                                .weight(1f)
                                .height(88.dp),
                        ) {}
                        SkeletonCard(
                            palette = palette,
                            modifier = Modifier
                                .weight(1f)
                                .height(88.dp),
                        ) {}
                    }
                    if (it < 2) {
                        Box(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SkeletonLine(
                    palette = palette,
                    width = 120.dp,
                    height = 18.dp,
                    modifier = Modifier.padding(horizontal = WeatherDimens.screenPadding),
                )
                Box(modifier = Modifier.height(14.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = WeatherDimens.screenPadding),
                ) {
                    items(listOf(1, 2, 3, 4, 5)) {
                        SkeletonCard(
                            palette = palette,
                            modifier = Modifier
                                .width(92.dp)
                                .height(112.dp),
                        ) {}
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WeatherDimens.screenPadding),
            ) {
                SkeletonLine(palette, width = 100.dp, height = 18.dp)
                Box(modifier = Modifier.height(14.dp))
                SkeletonCard(
                    palette = palette,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        repeat(5) {
                            SkeletonLine(palette, modifier = Modifier.fillMaxWidth(), height = 20.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SkeletonCard(
    palette: WeatherPalette,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .clip(RoundedCornerShape(WeatherDimens.cardCornerLarge))
            .background(palette.cardTint.copy(alpha = 0.18f))
            .padding(WeatherDimens.contentPadding),
        content = content,
    )
}

@Composable
private fun SkeletonLine(
    palette: WeatherPalette,
    modifier: Modifier = Modifier,
    width: androidx.compose.ui.unit.Dp = 160.dp,
    height: androidx.compose.ui.unit.Dp = 16.dp,
) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "skeleton-alpha",
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(palette.primaryText.copy(alpha = alpha))
            .width(width)
            .height(height),
    )
}
