package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.location.CurrentLocationFailure

sealed interface LoadCurrentLocationWeatherResult {
    data object Loading : LoadCurrentLocationWeatherResult
    data class Success(val weather: Weather) : LoadCurrentLocationWeatherResult
    data class LocationFailure(val error: CurrentLocationFailure) : LoadCurrentLocationWeatherResult
    data class WeatherFailureResult(val error: WeatherFailure) : LoadCurrentLocationWeatherResult
}
