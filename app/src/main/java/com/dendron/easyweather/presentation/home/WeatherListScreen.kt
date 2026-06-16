package com.dendron.easyweather.presentation.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.dendron.easyweather.R
import com.dendron.easyweather.presentation.home.components.CurrentWeatherConditions
import com.dendron.easyweather.presentation.home.components.CurrentWeatherImageAndDescription
import com.dendron.easyweather.presentation.home.components.DailyForecastSection
import com.dendron.easyweather.presentation.home.components.FirstRunPanel
import com.dendron.easyweather.presentation.home.components.HourlyForecastSection
import com.dendron.easyweather.presentation.home.components.LoadingWeatherSkeleton
import com.dendron.easyweather.presentation.home.components.ManualLocationPanel
import com.dendron.easyweather.presentation.home.components.WeatherStatusPanel
import com.dendron.easyweather.presentation.ui.theme.WeatherDimens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var isRequestingPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        isRequestingPermission = false
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
        onRefresh = viewModel::refreshData,
        onRetry = viewModel::retryData,
        onRequestPermission = {
            isRequestingPermission = true
            permissionLauncher.launch(locationPermissions)
        },
        onShowManualLocation = viewModel::showManualLocation,
        onManualLocationQueryChange = viewModel::updateManualLocationQuery,
        onManualLocationSearch = viewModel::searchManualLocations,
        onManualLocationSelect = viewModel::selectManualLocation,
        onManualLocationBack = viewModel::showEmptyState,
        onOpenAppSettings = context::openAppSettings,
        onOpenLocationSettings = context::openLocationSettings,
        isRequestingPermission = isRequestingPermission,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun HomeScreenContent(
    state: WeatherScreenState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onRequestPermission: () -> Unit,
    onShowManualLocation: () -> Unit,
    onManualLocationQueryChange: (String) -> Unit,
    onManualLocationSearch: () -> Unit,
    onManualLocationSelect: (com.dendron.easyweather.domain.location.SearchedLocation) -> Unit,
    onManualLocationBack: () -> Unit,
    onOpenAppSettings: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    isRequestingPermission: Boolean,
    modifier: Modifier = Modifier,
) {
    val palette = state.palette()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(palette.backgroundTop, palette.backgroundBottom),
                ),
            ),
    ) {
        when (val currentState = state) {
            WeatherScreenState.Empty -> FirstRunPanel(
                palette = palette,
                onPrimaryAction = onRequestPermission,
                onSecondaryAction = onShowManualLocation,
                modifier = Modifier.fillMaxSize(),
            )

            is WeatherScreenState.ManualLocation -> ManualLocationPanel(
                palette = palette,
                query = currentState.query,
                isSearching = currentState.isSearching,
                results = currentState.results,
                errorMessage = currentState.errorMessage,
                onQueryChange = onManualLocationQueryChange,
                onSearch = onManualLocationSearch,
                onSelect = onManualLocationSelect,
                onBack = onManualLocationBack,
                modifier = Modifier.fillMaxSize(),
            )

            is WeatherScreenState.Loading -> LoadingWeatherSkeleton(
                palette = palette,
                modifier = Modifier.fillMaxSize(),
            )

            is WeatherScreenState.Content -> WeatherContent(
                model = currentState.model,
                lastUpdatedAtMillis = currentState.lastUpdatedAtMillis,
                isRefreshing = currentState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
            )

            is WeatherScreenState.Error -> WeatherStatusPanel(
                palette = palette,
                title = stringResource(R.string.weather_error_title),
                message = stringResource(currentState.reason.messageResId),
                primaryActionLabel = stringResource(R.string.weather_retry_action),
                onPrimaryAction = onRetry,
                modifier = Modifier.fillMaxSize(),
            )

            WeatherScreenState.PermissionRequired -> WeatherStatusPanel(
                palette = palette,
                title = stringResource(R.string.weather_permission_title),
                message = stringResource(R.string.weather_permission_message),
                primaryActionLabel = stringResource(R.string.weather_permission_action),
                onPrimaryAction = onRequestPermission,
                secondaryActionLabel = stringResource(R.string.weather_open_settings_action),
                onSecondaryAction = onOpenAppSettings,
                modifier = Modifier.fillMaxSize(),
            )

            WeatherScreenState.LocationDisabled -> WeatherStatusPanel(
                palette = palette,
                title = stringResource(R.string.weather_location_disabled_title),
                message = stringResource(R.string.weather_location_disabled_message),
                primaryActionLabel = stringResource(R.string.weather_location_settings_action),
                onPrimaryAction = onOpenLocationSettings,
                secondaryActionLabel = stringResource(R.string.weather_retry_action),
                onSecondaryAction = onRetry,
                modifier = Modifier.fillMaxSize(),
            )
        }

        val feedbackMessage = when {
            isRequestingPermission -> stringResource(R.string.weather_permission_requesting_message)
            state is WeatherScreenState.Loading -> state.feedbackMessageResId?.let { stringResource(it) }
            state is WeatherScreenState.Content -> state.feedbackMessageResId?.let { stringResource(it) }
            else -> null
        }

        if (feedbackMessage != null) {
            FeedbackBanner(
                message = feedbackMessage,
                palette = palette,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = WeatherDimens.screenPadding),
            )
        }
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
            verticalArrangement = Arrangement.spacedBy(WeatherDimens.sectionSpacing),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                CurrentWeatherImageAndDescription(
                    model = model,
                    lastUpdatedAtMillis = lastUpdatedAtMillis,
                    modifier = Modifier.padding(top = WeatherDimens.screenPadding),
                )
            }
            item {
                CurrentWeatherConditions(model = model)
            }
            if (model.hourlyForecast.isNotEmpty()) {
                item {
                    HourlyForecastSection(
                        items = model.hourlyForecast,
                        palette = model.palette,
                    )
                }
            }
            if (model.dailyForecast.isNotEmpty()) {
                item {
                    DailyForecastSection(
                        items = model.dailyForecast,
                        palette = model.palette,
                        modifier = Modifier.padding(bottom = WeatherDimens.screenPadding),
                    )
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun FeedbackBanner(
    message: String,
    palette: WeatherPalette,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = palette.cardTint.copy(alpha = 0.22f),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(horizontal = WeatherDimens.screenPadding),
    ) {
        Text(
            text = message,
            color = palette.primaryText,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = WeatherDimens.largeSpacing, vertical = WeatherDimens.mediumSpacing),
        )
    }
}

private fun WeatherScreenState.palette(): WeatherPalette = when (this) {
    is WeatherScreenState.Content -> model.palette
    else -> DefaultWeatherPalette
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
