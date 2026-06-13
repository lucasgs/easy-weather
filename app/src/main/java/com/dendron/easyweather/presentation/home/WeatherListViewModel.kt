package com.dendron.easyweather.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.easyweather.R
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
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherScreenState>(WeatherScreenState.Empty)
    val state = _state.asStateFlow()

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
                                    model = result.data.toUiModel(),
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

private fun getWeatherDescription(weatherCode: Int): String =
    when (weatherCode) {
        0 -> "Clear sky"
        1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
        45, 48 -> "Fog and depositing rime fog"
        51, 53, 55 -> "Drizzle: Light, moderate, and dense intensity"
        56, 57 -> "Freezing Drizzle: Light and dense intensity"
        61, 63, 65 -> "Rain: Slight, moderate and heavy intensity"
        66, 67 -> "Freezing Rain: Light and heavy intensity"
        71, 73, 75 -> "Snow fall: Slight, moderate, and heavy intensity"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers: Slight, moderate, and violent"
        85, 86 -> "Snow showers slight and heavy"
        95 -> "Thunderstorm: Slight or moderate"
        96, 99 -> "Thunderstorm with slight and heavy hail"
        else -> "Nothing to say :("
    }

private fun getWeatherImage(weatherCode: Int): Int =
    when (weatherCode) {
        0 -> R.drawable.day_sunny
        1, 2, 3 -> R.drawable.cloudy
        45, 48 -> R.drawable.fog
        51, 53, 55, 56, 57 -> R.drawable.drizzle
        61, 63, 65, 66, 67 -> R.drawable.rain
        71, 73, 75, 77 -> R.drawable.snow
        80, 81, 82, 85, 86 -> R.drawable.showers
        95, 96, 99 -> R.drawable.thunderstorm
        else -> R.drawable.na
    }

private fun getWindDirection(angle: Int): String =
    when (angle) {
        in 0..22 -> "N"
        in 22..67 -> "NE"
        in 67..112 -> "E"
        in 112..157 -> "SE"
        in 157..202 -> "S"
        in 202..247 -> "SW"
        in 247..292 -> "W"
        in 292..337 -> "NW"
        else -> "No direction"
    }

fun Weather.toUiModel(): WeatherUiModel {
    val windDirectionText = getWindDirection(windDirection)
    return WeatherUiModel(
        weatherIcon = getWeatherImage(weatherCode),
        descriptionText = getWeatherDescription(weatherCode),
        windText = "$windSpeed ${weatherUnit.wind} $windDirectionText",
        temperatureText = "$minTemperature° / $currentTemperature° / $maxTemperature°",
    )
}
