package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.domain.location.SearchedLocation

sealed interface WeatherScreenState {
    data object Empty : WeatherScreenState
    data class Loading(
        val feedbackMessageResId: Int? = null,
    ) : WeatherScreenState
    data object PermissionRequired : WeatherScreenState
    data object LocationDisabled : WeatherScreenState

    data class ManualLocation(
        val query: String = "",
        val isSearching: Boolean = false,
        val results: List<SearchedLocation> = emptyList(),
        val errorMessage: String? = null,
    ) : WeatherScreenState

    data class Content(
        val model: WeatherUiModel,
        val lastUpdatedAtMillis: Long,
        val isRefreshing: Boolean = false,
        val feedbackMessageResId: Int? = null,
        val isStale: Boolean = false,
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
