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
import com.dendron.easyweather.presentation.ui.theme.WhiteDark
import com.dendron.easyweather.presentation.ui.theme.WhiteLight

@Composable
fun ManualLocationPanel(
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
        color = WhiteLight.copy(alpha = 0.08f),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(
                text = stringResource(R.string.weather_manual_title),
                color = WhiteLight,
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.weather_manual_message),
                color = WhiteDark,
                style = MaterialTheme.typography.body1,
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text(stringResource(R.string.weather_manual_search_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onSearch) {
                    Text(if (isSearching) stringResource(R.string.weather_loading_title) else stringResource(R.string.weather_manual_search_action))
                }
                OutlinedButton(onClick = onBack) {
                    Text(stringResource(R.string.weather_manual_back_action))
                }
            }
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = WhiteDark,
                    style = MaterialTheme.typography.body2,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(results) { location ->
                    Surface(
                        color = WhiteLight.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(location) },
                    ) {
                        Text(
                            text = location.displayName,
                            color = WhiteLight,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}
