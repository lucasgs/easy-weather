package com.dendron.easyweather.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dendron.easyweather.R
import com.dendron.easyweather.domain.location.SearchedLocation
import com.dendron.easyweather.presentation.home.WeatherPalette
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@Composable
fun ManualLocationPanel(
    palette: WeatherPalette,
    query: String,
    isSearching: Boolean,
    results: List<SearchedLocation>,
    errorMessage: String?,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSelect: (SearchedLocation) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = palette.cardTint.copy(alpha = 0.14f),
        shape = RoundedCornerShape(WeatherDimens.cardCornerLarge),
        modifier = modifier
            .fillMaxSize()
            .padding(WeatherDimens.screenPadding),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WeatherDimens.contentPadding),
        ) {
            Text(
                text = stringResource(R.string.weather_manual_title),
                color = palette.primaryText,
                style = MaterialTheme.typography.h4,
            )
            Spacer(modifier = Modifier.height(WeatherDimens.compactSpacing))
            Text(
                text = stringResource(R.string.weather_manual_message),
                color = palette.secondaryText,
                style = MaterialTheme.typography.body1,
            )
            Spacer(modifier = Modifier.height(WeatherDimens.sectionSpacing))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text(stringResource(R.string.weather_manual_search_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(WeatherDimens.mediumSpacing))
            Row(
                horizontalArrangement = Arrangement.spacedBy(WeatherDimens.mediumSpacing),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = onSearch,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (isSearching) stringResource(R.string.weather_loading_title) else stringResource(R.string.weather_manual_search_action))
                }
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.weather_manual_back_action))
                }
            }
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(WeatherDimens.mediumSpacing))
                Text(
                    text = errorMessage,
                    color = palette.secondaryText,
                    style = MaterialTheme.typography.body2,
                )
            }
            Spacer(modifier = Modifier.height(WeatherDimens.largeSpacing))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(results) { location ->
                    Surface(
                        color = palette.primaryText.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(WeatherDimens.cardCornerSmall),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(location) },
                    ) {
                        Text(
                            text = location.displayName,
                            color = palette.primaryText,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(WeatherDimens.largeSpacing),
                        )
                    }
                }
            }
        }
    }
}
