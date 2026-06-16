package com.dendron.easyweather.domain.location

sealed interface CurrentLocationResult {
    data class Success(val data: LocationData) : CurrentLocationResult
    data class Failure(val error: CurrentLocationFailure) : CurrentLocationResult
}

sealed interface CurrentLocationFailure {
    data object PermissionDenied : CurrentLocationFailure
    data object LocationDisabled : CurrentLocationFailure
    data object Unavailable : CurrentLocationFailure
}
