package com.dendron.easyweather.domain

sealed interface WeatherResult {
    data object Loading : WeatherResult
    data class Success(val weather: Weather) : WeatherResult
    data class Failure(val error: WeatherFailure) : WeatherResult
}

sealed interface WeatherFailure {
    data object Network : WeatherFailure
    data object Unknown : WeatherFailure
}
