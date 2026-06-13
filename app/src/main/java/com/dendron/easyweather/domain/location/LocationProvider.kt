package com.dendron.easyweather.domain.location

sealed interface LocationResult {
    data class Success(val data: LocationData) : LocationResult
    data object PermissionDenied : LocationResult
    data object LocationDisabled : LocationResult
    data object Unavailable : LocationResult
}

interface LocationProvider {
    suspend fun getCurrentLocation(): LocationResult
}
