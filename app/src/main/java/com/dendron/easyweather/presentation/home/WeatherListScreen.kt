package com.dendron.easyweather.presentation.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.components.CurrentWeatherConditions
import com.dendron.easyweather.presentation.home.components.CurrentWeatherImageAndDescription
import com.dendron.easyweather.presentation.home.components.FirstRunPanel
import com.dendron.easyweather.presentation.home.components.ManualLocationPanel
import com.dendron.easyweather.presentation.home.components.WeatherStatusPanel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        if (permissions.values.all { it }) {
            viewModel.fetchData()
        } else {
            viewModel.showPermissionRequired()
        }
    }

    LaunchedEffect(Unit) {
        if (context.hasLocationPermissions()) {
            viewModel.fetchData()
        } else {
            viewModel.showEmptyState()
        }
    }

    HomeScreenContent(
        state = state,
        onRefresh = viewModel::fetchData,
        onRequestPermission = {
            permissionLauncher.launch(locationPermissions)
        },
        onShowManualLocation = viewModel::showManualLocation,
        onManualLocationQueryChange = viewModel::updateManualLocationQuery,
        onManualLocationSearch = viewModel::searchManualLocations,
        onManualLocationSelect = viewModel::selectManualLocation,
        onManualLocationBack = viewModel::showEmptyState,
        onOpenAppSettings = context::openAppSettings,
        onOpenLocationSettings = context::openLocationSettings,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun HomeScreenContent(
    state: WeatherScreenState,
    onRefresh: () -> Unit,
    onRequestPermission: () -> Unit,
    onShowManualLocation: () -> Unit,
    onManualLocationQueryChange: (String) -> Unit,
    onManualLocationSearch: () -> Unit,
    onManualLocationSelect: (com.dendron.easyweather.domain.location.SearchedLocation) -> Unit,
    onManualLocationBack: () -> Unit,
    onOpenAppSettings: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (val currentState = state) {
        WeatherScreenState.Empty -> FirstRunPanel(
            onPrimaryAction = onRequestPermission,
            onSecondaryAction = onShowManualLocation,
            modifier = modifier,
        )

        is WeatherScreenState.ManualLocation -> ManualLocationPanel(
            query = currentState.query,
            isSearching = currentState.isSearching,
            results = currentState.results,
            errorMessage = currentState.errorMessage,
            onQueryChange = onManualLocationQueryChange,
            onSearch = onManualLocationSearch,
            onSelect = onManualLocationSelect,
            onBack = onManualLocationBack,
            modifier = modifier,
        )

        WeatherScreenState.Loading -> WeatherStatusPanel(
            title = stringResource(R.string.weather_loading_title),
            message = stringResource(R.string.weather_loading_message),
            modifier = modifier,
        )

        is WeatherScreenState.Content -> WeatherContent(
            model = currentState.model,
            lastUpdatedAtMillis = currentState.lastUpdatedAtMillis,
            isRefreshing = currentState.isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier,
        )

        is WeatherScreenState.Error -> WeatherStatusPanel(
            title = stringResource(R.string.weather_error_title),
            message = stringResource(currentState.reason.messageResId),
            primaryActionLabel = stringResource(R.string.weather_retry_action),
            onPrimaryAction = onRefresh,
            modifier = modifier,
        )

        WeatherScreenState.PermissionRequired -> WeatherStatusPanel(
            title = stringResource(R.string.weather_permission_title),
            message = stringResource(R.string.weather_permission_message),
            primaryActionLabel = stringResource(R.string.weather_permission_action),
            onPrimaryAction = onRequestPermission,
            secondaryActionLabel = stringResource(R.string.weather_open_settings_action),
            onSecondaryAction = onOpenAppSettings,
            modifier = modifier,
        )

        WeatherScreenState.LocationDisabled -> WeatherStatusPanel(
            title = stringResource(R.string.weather_location_disabled_title),
            message = stringResource(R.string.weather_location_disabled_message),
            primaryActionLabel = stringResource(R.string.weather_location_settings_action),
            onPrimaryAction = onOpenLocationSettings,
            secondaryActionLabel = stringResource(R.string.weather_retry_action),
            onSecondaryAction = onRefresh,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WeatherContent(
    model: WeatherUiModel,
    lastUpdatedAtMillis: Long,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.pullRefresh(refreshState),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                CurrentWeatherImageAndDescription(
                    model = model,
                    lastUpdatedAtMillis = lastUpdatedAtMillis,
                )
            }
            item {
                CurrentWeatherConditions(model = model)
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

private val ErrorReason.messageResId: Int
    get() = when (this) {
        ErrorReason.Network -> R.string.weather_error_network_message
        ErrorReason.LocationUnavailable -> R.string.weather_error_location_unavailable_message
        ErrorReason.Unknown -> R.string.weather_error_unknown_message
    }

private val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

private fun Context.hasLocationPermissions(): Boolean =
    locationPermissions.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

private fun Context.openAppSettings() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null),
        ),
    )
}

private fun Context.openLocationSettings() {
    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
}
