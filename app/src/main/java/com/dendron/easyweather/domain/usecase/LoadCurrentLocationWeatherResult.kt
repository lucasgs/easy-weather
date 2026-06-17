package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.location.CurrentLocationFailure
import com.dendron.easyweather.domain.location.LocationData

sealed interface LoadCurrentLocationWeatherResult {
    data object Loading : LoadCurrentLocationWeatherResult
    data class Success(
        val weather: Weather,
        val locationData: LocationData,
        val lastUpdatedAtMillis: Long,
        val isStale: Boolean = false,
        val isFromCache: Boolean = false,
    ) : LoadCurrentLocationWeatherResult
    data class LocationFailure(val error: CurrentLocationFailure) : LoadCurrentLocationWeatherResult
    data class WeatherFailureResult(val error: WeatherFailure) : LoadCurrentLocationWeatherResult
}
