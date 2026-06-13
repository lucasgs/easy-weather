package com.dendron.easyweather.presentation.home

sealed interface WeatherScreenState {
    data object Empty : WeatherScreenState
    data object Loading : WeatherScreenState
    data object PermissionRequired : WeatherScreenState
    data object LocationDisabled : WeatherScreenState

    data class Content(
        val model: WeatherUiModel,
        val lastUpdatedAtMillis: Long,
        val isRefreshing: Boolean = false,
    ) : WeatherScreenState

    data class Error(
        val reason: ErrorReason,
    ) : WeatherScreenState
}

enum class ErrorReason {
    Network,
    LocationUnavailable,
    Unknown,
}
