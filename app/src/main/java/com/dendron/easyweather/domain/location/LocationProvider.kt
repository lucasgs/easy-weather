package com.dendron.easyweather.domain.location

interface LocationProvider {
    suspend fun getCurrentLocation(): CurrentLocationResult
}
