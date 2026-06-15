package com.dendron.easyweather.presentation.home.components

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.presentation.home.WeatherPalette
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@Composable
fun LoadingWeatherSkeleton(
    palette: WeatherPalette,
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberSkeletonBrush(palette)

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
                    .height(236.dp),
            ) {
                SkeletonLine(shimmer, width = 112.dp, color = palette.secondaryText)
                SkeletonLine(shimmer, width = 64.dp, height = 12.dp, color = palette.secondaryText)
                Box(modifier = Modifier.height(WeatherDimens.mediumSpacing))
                Row(horizontalArrangement = Arrangement.spacedBy(WeatherDimens.largeSpacing)) {
                    SkeletonBlock(
                        shimmer = shimmer,
                        width = 88.dp,
                        height = 88.dp,
                        cornerRadius = WeatherDimens.cardCornerSmall,
                        color = palette.accent,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing)) {
                        SkeletonLine(shimmer, width = 120.dp, height = 42.dp)
                        SkeletonLine(shimmer, width = 168.dp)
                        SkeletonLine(shimmer, width = 104.dp, color = palette.secondaryText)
                    }
                }
                Box(modifier = Modifier.height(WeatherDimens.compactSpacing))
                SkeletonLine(shimmer, width = 144.dp, color = palette.secondaryText)
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WeatherDimens.screenPadding),
            ) {
                SkeletonLine(shimmer, width = 136.dp, height = 18.dp)
                Box(modifier = Modifier.height(14.dp))
                repeat(3) {
                    Row(horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing)) {
                        SkeletonMetricCard(palette, shimmer, Modifier.weight(1f))
                        SkeletonMetricCard(palette, shimmer, Modifier.weight(1f))
                    }
                    if (it < 2) {
                        Box(modifier = Modifier.height(WeatherDimens.mediumSpacing))
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SkeletonLine(
                    shimmer = shimmer,
                    width = 120.dp,
                    height = 18.dp,
                    modifier = Modifier.padding(horizontal = WeatherDimens.screenPadding),
                )
                Box(modifier = Modifier.height(14.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing),
                    contentPadding = PaddingValues(horizontal = WeatherDimens.screenPadding),
                ) {
                    items(listOf(1, 2, 3, 4, 5)) {
                        SkeletonCard(
                            palette = palette,
                            modifier = Modifier
                                .width(96.dp)
                                .height(116.dp),
                        ) {
                            SkeletonLine(shimmer, width = 36.dp, height = 10.dp, color = palette.secondaryText)
                            Box(modifier = Modifier.height(12.dp))
                            SkeletonLine(shimmer, width = 48.dp, height = 22.dp)
                            SkeletonLine(shimmer, width = 42.dp, height = 14.dp, color = palette.secondaryText)
                        }
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
                SkeletonLine(shimmer, width = 100.dp, height = 18.dp)
                Box(modifier = Modifier.height(14.dp))
                SkeletonCard(
                    palette = palette,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(5) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            SkeletonLine(shimmer, width = 44.dp, color = palette.secondaryText)
                            SkeletonLine(shimmer, width = 72.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SkeletonMetricCard(
    palette: WeatherPalette,
    shimmer: Brush,
    modifier: Modifier = Modifier,
) {
    SkeletonCard(
        palette = palette,
        modifier = modifier.height(96.dp),
    ) {
        SkeletonLine(shimmer, width = 64.dp, height = 10.dp, color = palette.secondaryText)
        SkeletonLine(shimmer, width = 92.dp, height = 18.dp)
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
            .background(palette.cardTint.copy(alpha = 0.16f))
            .padding(WeatherDimens.contentPadding),
        content = content,
    )
}

@Composable
private fun SkeletonLine(
    shimmer: Brush,
    modifier: Modifier = Modifier,
    width: Dp = 160.dp,
    height: Dp = 16.dp,
    color: Color = Color.White,
) {
    SkeletonBlock(
        shimmer = shimmer,
        width = width,
        height = height,
        cornerRadius = 999.dp,
        color = color,
        modifier = modifier,
    )
}

@Composable
private fun SkeletonBlock(
    shimmer: Brush,
    width: Dp,
    height: Dp,
    cornerRadius: Dp,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(color.copy(alpha = 0.18f))
            .background(shimmer),
    )
}

@Composable
private fun rememberSkeletonBrush(palette: WeatherPalette): Brush {
    val transition = rememberInfiniteTransition(label = "skeleton-shimmer")
    val xShift by transition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "skeleton-shift",
    )

    return Brush.linearGradient(
        colors = listOf(
            palette.primaryText.copy(alpha = 0.10f),
            palette.primaryText.copy(alpha = 0.28f),
            palette.primaryText.copy(alpha = 0.10f),
        ),
        start = Offset(xShift - 220f, 0f),
        end = Offset(xShift, 280f),
    )
}
