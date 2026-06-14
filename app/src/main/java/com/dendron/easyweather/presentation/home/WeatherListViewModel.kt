package com.dendron.easyweather.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationResult
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
                    weatherRepository.getCurrentWeather(
                        latitude = currentLocation.data.latitude,
                        longitude = currentLocation.data.longitude,
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
    }
}

private fun Resource.Error<Weather>.toErrorReason(): ErrorReason =
    if (message.isNullOrBlank()) {
        ErrorReason.Unknown
    } else {
        ErrorReason.Network
    }
