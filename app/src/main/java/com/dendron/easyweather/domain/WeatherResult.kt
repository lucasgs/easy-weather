package com.dendron.easyweather.domain

sealed interface WeatherResult {
    data object Loading : WeatherResult
    data class Success(
        val weather: Weather,
        val lastUpdatedAtMillis: Long,
        val isStale: Boolean = false,
        val isFromCache: Boolean = false,
    ) : WeatherResult
    data class Failure(val error: WeatherFailure) : WeatherResult
}

sealed interface WeatherFailure {
    data object Network : WeatherFailure
    data object Timeout : WeatherFailure
    data object Unknown : WeatherFailure
}
