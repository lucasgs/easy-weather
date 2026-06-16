package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationResult
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
) {
    suspend operator fun invoke(): LocationResult = locationProvider.getCurrentLocation()
}
