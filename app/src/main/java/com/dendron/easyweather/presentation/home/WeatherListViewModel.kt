package com.dendron.easyweather.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationResult
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.location.SearchedLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider,
    private val locationSearchRepository: LocationSearchRepository,
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
            when (val result = locationSearchRepository.searchLocations(query)) {
                is Resource.Success -> {
                    _state.value = current.copy(
                        query = query,
                        isSearching = false,
                        results = result.data,
                        errorMessage = if (result.data.isEmpty()) "No matching cities found." else null,
                    )
                }

                is Resource.Error -> {
                    _state.value = current.copy(
                        query = query,
                        isSearching = false,
                        errorMessage = result.message ?: "Could not search for that city.",
                    )
                }

                is Resource.Loading -> Unit
            }
        }
    }

    fun selectManualLocation(location: SearchedLocation) {
        fetchWeather(latitude = location.latitude, longitude = location.longitude)
    }

    fun fetchData() {
        val currentContent = _state.value as? WeatherScreenState.Content
        _state.value = currentContent?.copy(isRefreshing = true) ?: WeatherScreenState.Loading

        viewModelScope.launch {
            when (val currentLocation = locationProvider.getCurrentLocation()) {
                LocationResult.PermissionDenied -> {
                    _state.value = WeatherScreenState.PermissionRequired
                }

                LocationResult.LocationDisabled -> {
                    _state.value = WeatherScreenState.LocationDisabled
                }

                LocationResult.Unavailable -> {
                    _state.value = WeatherScreenState.Error(ErrorReason.LocationUnavailable)
                }

                is LocationResult.Success -> {
                    fetchWeather(
                        latitude = currentLocation.data.latitude,
                        longitude = currentLocation.data.longitude,
                    )
                }
            }
        }
    }

    private fun fetchWeather(latitude: Double, longitude: Double) {
        val currentContent = _state.value as? WeatherScreenState.Content
        _state.value = currentContent?.copy(isRefreshing = true) ?: WeatherScreenState.Loading

        viewModelScope.launch {
            weatherRepository.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = WeatherScreenState.Content(
                            model = weatherUiModelMapper.map(result.data),
                            lastUpdatedAtMillis = System.currentTimeMillis(),
                        )
                    }

                    is Resource.Error -> {
                        _state.value = WeatherScreenState.Error(result.toErrorReason())
                    }

                    is Resource.Loading -> {
                        _state.value = currentContent?.copy(isRefreshing = true)
                            ?: WeatherScreenState.Loading
                    }
                }
            }
        }
    }
}

private fun Resource.Error<Weather>.toErrorReason(): ErrorReason =
    if (message.isNullOrBlank()) {
        ErrorReason.Unknown
    } else {
        ErrorReason.Network
    }
