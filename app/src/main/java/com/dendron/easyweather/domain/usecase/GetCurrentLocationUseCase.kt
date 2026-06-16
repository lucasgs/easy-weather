package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.location.CurrentLocationResult
import com.dendron.easyweather.domain.location.LocationProvider
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
) {
    suspend operator fun invoke(): CurrentLocationResult = locationProvider.getCurrentLocation()
}
