package com.dendron.easyweather.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.easyweather.R
import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.WeatherResult
import com.dendron.easyweather.domain.location.CurrentLocationFailure
import com.dendron.easyweather.domain.location.CurrentLocationResult
import com.dendron.easyweather.domain.location.LocationSearchFailure
import com.dendron.easyweather.domain.location.LocationSearchResult
import com.dendron.easyweather.domain.location.SearchedLocation
import com.dendron.easyweather.domain.usecase.LoadCurrentLocationWeatherResult
import com.dendron.easyweather.domain.usecase.LoadCurrentLocationWeatherUseCase
import com.dendron.easyweather.domain.usecase.LoadWeatherForCoordinatesUseCase
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
    private val weatherUiModelMapper: WeatherUiModelMapper,
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherScreenState>(WeatherScreenState.Empty)
    val state = _state.asStateFlow()

    fun showEmptyState() {
        _state.value = WeatherScreenState.Empty
    }

    fun showPermissionRequired() {
        _state.value = WeatherScreenState.PermissionRequired
    }

    fun showManualLocation() {
        _state.value = WeatherScreenState.ManualLocation()
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
        )
    }

    fun fetchData() {
        loadCurrentLocationWeather(R.string.weather_loading_message)
    }

    fun refreshData() {
        loadCurrentLocationWeather(R.string.weather_refreshing_message)
    }

    fun retryData() {
        loadCurrentLocationWeather(R.string.weather_retrying_message)
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
                        _state.value = WeatherScreenState.Content(
                            model = weatherUiModelMapper.map(result.weather),
                            lastUpdatedAtMillis = System.currentTimeMillis(),
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
                                _state.value = WeatherScreenState.Error(ErrorReason.LocationUnavailable)
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
                        _state.value = WeatherScreenState.Content(
                            model = weatherUiModelMapper.map(result.weather),
                            lastUpdatedAtMillis = System.currentTimeMillis(),
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

    private fun showLoadingState(
        currentContent: WeatherScreenState.Content?,
        feedbackMessageResId: Int,
    ) {
        _state.value = currentContent?.copy(
            isRefreshing = true,
            feedbackMessageResId = feedbackMessageResId,
        ) ?: WeatherScreenState.Loading(feedbackMessageResId)
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
