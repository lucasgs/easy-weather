package com.dendron.easyweather.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.easyweather.R
import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.WeatherResult
import com.dendron.easyweather.domain.location.CurrentLocationFailure
import com.dendron.easyweather.domain.location.CurrentLocationResult
import com.dendron.easyweather.domain.location.LocationData
import com.dendron.easyweather.domain.location.LocationSearchFailure
import com.dendron.easyweather.domain.location.LocationSearchResult
import com.dendron.easyweather.domain.location.SearchedLocation
import com.dendron.easyweather.domain.preferences.SavedLocationPreference
import com.dendron.easyweather.domain.preferences.SavedLocationSource
import com.dendron.easyweather.domain.usecase.GetWeatherPreferencesUseCase
import com.dendron.easyweather.domain.usecase.LoadCurrentLocationWeatherResult
import com.dendron.easyweather.domain.usecase.LoadCurrentLocationWeatherUseCase
import com.dendron.easyweather.domain.usecase.LoadWeatherForCoordinatesUseCase
import com.dendron.easyweather.domain.usecase.SaveLastLocationUseCase
import com.dendron.easyweather.domain.usecase.SearchLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val loadCurrentLocationWeatherUseCase: LoadCurrentLocationWeatherUseCase,
    private val loadWeatherForCoordinatesUseCase: LoadWeatherForCoordinatesUseCase,
    private val searchLocationsUseCase: SearchLocationsUseCase,
    private val getWeatherPreferencesUseCase: GetWeatherPreferencesUseCase,
    private val saveLastLocationUseCase: SaveLastLocationUseCase,
    private val weatherUiModelMapper: WeatherUiModelMapper,
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherScreenState>(WeatherScreenState.Empty)
    private var previousContentState: WeatherScreenState.Content? = null
    val state = _state.asStateFlow()

    fun showEmptyState() {
        _state.value = WeatherScreenState.Empty
    }

    fun showPermissionRequired() {
        _state.value = WeatherScreenState.PermissionRequired
    }

    fun showManualLocation() {
        previousContentState = _state.value as? WeatherScreenState.Content ?: previousContentState
        val current = _state.value as? WeatherScreenState.ManualLocation
        _state.value = current ?: WeatherScreenState.ManualLocation()
    }

    fun dismissManualLocation() {
        _state.value = previousContentState ?: WeatherScreenState.Empty
    }

    fun updateManualLocationQuery(query: String) {
        val current = _state.value as? WeatherScreenState.ManualLocation ?: WeatherScreenState.ManualLocation()
        _state.value = current.copy(query = query, errorMessage = null)
    }

    fun searchManualLocations() {
        val current = _state.value as? WeatherScreenState.ManualLocation ?: return
        val query = current.query.trim()
        if (query.isBlank()) {
            _state.value = current.copy(errorMessage = "Enter a city name to search.")
            return
        }

        _state.value = current.copy(isSearching = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = searchLocationsUseCase(query)) {
                is LocationSearchResult.Success -> {
                    _state.value = current.copy(
                        query = query,
                        isSearching = false,
                        results = result.locations,
                        errorMessage = if (result.locations.isEmpty()) "No matching cities found." else null,
                    )
                }

                is LocationSearchResult.Failure -> {
                    _state.value = current.copy(
                        query = query,
                        isSearching = false,
                        errorMessage = result.error.toErrorMessage(),
                    )
                }
            }
        }
    }

    fun selectManualLocation(location: SearchedLocation) {
        fetchWeather(
            latitude = location.latitude,
            longitude = location.longitude,
            feedbackMessageResId = R.string.weather_loading_message,
            savedLocation = SavedLocationPreference(
                latitude = location.latitude,
                longitude = location.longitude,
                name = location.displayName,
                source = SavedLocationSource.Manual,
            ),
        )
    }

    fun fetchStartupData(hasLocationPermissions: Boolean) {
        viewModelScope.launch {
            when (val lastLocation = getWeatherPreferencesUseCase().lastLocation) {
                null -> if (hasLocationPermissions) {
                    loadCurrentLocationWeather(R.string.weather_loading_message)
                } else {
                    showEmptyState()
                }

                else -> if (lastLocation.source == SavedLocationSource.Manual || !hasLocationPermissions) {
                    fetchWeather(
                        latitude = lastLocation.latitude,
                        longitude = lastLocation.longitude,
                        feedbackMessageResId = R.string.weather_loading_message,
                        savedLocation = lastLocation,
                    )
                } else {
                    loadCurrentLocationWeather(R.string.weather_loading_message)
                }
            }
        }
    }

    fun fetchData() {
        loadCurrentLocationWeather(R.string.weather_loading_message)
    }

    fun refreshData() {
        refreshLastViewedWeather(R.string.weather_refreshing_message)
    }

    fun retryData() {
        refreshLastViewedWeather(R.string.weather_retrying_message)
    }

    private fun loadCurrentLocationWeather(feedbackMessageResId: Int) {
        val currentContent = _state.value as? WeatherScreenState.Content
        val isRefresh = currentContent != null || feedbackMessageResId != R.string.weather_loading_message
        showLoadingState(currentContent, feedbackMessageResId)

        viewModelScope.launch {
            loadCurrentLocationWeatherUseCase(isRefresh).collect { result ->
                when (result) {
                    LoadCurrentLocationWeatherResult.Loading -> {
                        showLoadingState(currentContent, feedbackMessageResId)
                    }

                    is LoadCurrentLocationWeatherResult.Success -> {
                        saveLastViewedLocation(
                            location = SavedLocationPreference(
                                latitude = result.locationData.latitude,
                                longitude = result.locationData.longitude,
                                name = result.weather.locationName,
                                source = SavedLocationSource.Current,
                            ),
                        )
                        showWeatherContent(
                            weather = result.weather,
                            lastUpdatedAtMillis = result.lastUpdatedAtMillis,
                            isStale = result.isStale,
                            isFromCache = result.isFromCache,
                        )
                    }

                    is LoadCurrentLocationWeatherResult.LocationFailure -> {
                        when (result.error) {
                            CurrentLocationFailure.PermissionDenied -> {
                                _state.value = WeatherScreenState.PermissionRequired
                            }

                            CurrentLocationFailure.LocationDisabled -> {
                                _state.value = WeatherScreenState.LocationDisabled
                            }

                            CurrentLocationFailure.Unavailable -> {
                                if (!restoreSavedLocation(feedbackMessageResId)) {
                                    _state.value = WeatherScreenState.Error(ErrorReason.LocationUnavailable)
                                }
                            }
                        }
                    }

                    is LoadCurrentLocationWeatherResult.WeatherFailureResult -> {
                        _state.value = WeatherScreenState.Error(result.error.toErrorReason())
                    }
                }
            }
        }
    }

    private fun fetchWeather(
        latitude: Double,
        longitude: Double,
        feedbackMessageResId: Int,
        savedLocation: SavedLocationPreference,
    ) {
        val currentContent = _state.value as? WeatherScreenState.Content
        val isRefresh = currentContent != null || feedbackMessageResId != R.string.weather_loading_message
        showLoadingState(currentContent, feedbackMessageResId)

        viewModelScope.launch {
            loadWeatherForCoordinatesUseCase(
                latitude = latitude,
                longitude = longitude,
                isRefresh = isRefresh,
            ).collect { result ->
                when (result) {
                    is WeatherResult.Success -> {
                        saveLastViewedLocation(savedLocation)
                        showWeatherContent(
                            weather = result.weather,
                            lastUpdatedAtMillis = result.lastUpdatedAtMillis,
                            isStale = result.isStale,
                            isFromCache = result.isFromCache,
                        )
                    }

                    is WeatherResult.Failure -> {
                        _state.value = WeatherScreenState.Error(result.error.toErrorReason())
                    }

                    WeatherResult.Loading -> {
                        showLoadingState(currentContent, feedbackMessageResId)
                    }
                }
            }
        }
    }

    private fun refreshLastViewedWeather(feedbackMessageResId: Int) {
        viewModelScope.launch {
            val lastLocation = getWeatherPreferencesUseCase().lastLocation
            if (lastLocation?.source == SavedLocationSource.Manual) {
                fetchWeather(
                    latitude = lastLocation.latitude,
                    longitude = lastLocation.longitude,
                    feedbackMessageResId = feedbackMessageResId,
                    savedLocation = lastLocation,
                )
            } else {
                loadCurrentLocationWeather(feedbackMessageResId)
            }
        }
    }

    private suspend fun restoreSavedLocation(feedbackMessageResId: Int): Boolean {
        val lastLocation = getWeatherPreferencesUseCase().lastLocation ?: return false
        fetchWeather(
            latitude = lastLocation.latitude,
            longitude = lastLocation.longitude,
            feedbackMessageResId = feedbackMessageResId,
            savedLocation = lastLocation,
        )
        return true
    }

    private fun saveLastViewedLocation(location: SavedLocationPreference) {
        viewModelScope.launch {
            saveLastLocationUseCase(location)
        }
    }

    private fun showLoadingState(
        currentContent: WeatherScreenState.Content?,
        feedbackMessageResId: Int,
    ) {
        _state.value = currentContent?.copy(
            isRefreshing = true,
            feedbackMessageResId = feedbackMessageResId,
        ) ?: WeatherScreenState.Loading(feedbackMessageResId)
    }

    private fun showWeatherContent(
        weather: com.dendron.easyweather.domain.Weather,
        lastUpdatedAtMillis: Long,
        isStale: Boolean,
        isFromCache: Boolean,
    ) {
        val content = WeatherScreenState.Content(
            model = weatherUiModelMapper.map(weather),
            lastUpdatedAtMillis = lastUpdatedAtMillis,
            feedbackMessageResId = when {
                isStale -> R.string.weather_cached_stale_message
                isFromCache -> R.string.weather_cached_refreshing_message
                else -> null
            },
            isStale = isStale,
        )
        previousContentState = content
        _state.value = content
    }
}

private fun WeatherFailure.toErrorReason(): ErrorReason = when (this) {
    WeatherFailure.Network,
    WeatherFailure.Timeout,
    -> ErrorReason.Network

    WeatherFailure.Unknown -> ErrorReason.Unknown
}

private fun LocationSearchFailure.toErrorMessage(): String = when (this) {
    LocationSearchFailure.Network,
    LocationSearchFailure.Timeout,
    -> "Could not search for that city."

    LocationSearchFailure.Unknown -> "Something went wrong while searching."
}
