package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.WeatherResult
import com.dendron.easyweather.domain.location.CurrentLocationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadCurrentLocationWeatherUseCase @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val loadWeatherForCoordinatesUseCase: LoadWeatherForCoordinatesUseCase,
) {
    operator fun invoke(isRefresh: Boolean): Flow<LoadCurrentLocationWeatherResult> = flow {
        when (val currentLocation = getCurrentLocationUseCase()) {
            is CurrentLocationResult.Success -> {
                loadWeatherForCoordinatesUseCase(
                    latitude = currentLocation.data.latitude,
                    longitude = currentLocation.data.longitude,
                    isRefresh = isRefresh,
                ).collect { result ->
                    when (result) {
                        WeatherResult.Loading -> emit(LoadCurrentLocationWeatherResult.Loading)
                        is WeatherResult.Success -> emit(
                            LoadCurrentLocationWeatherResult.Success(
                                weather = result.weather,
                                locationData = currentLocation.data,
                                lastUpdatedAtMillis = result.lastUpdatedAtMillis,
                                isStale = result.isStale,
                                isFromCache = result.isFromCache,
                            ),
                        )
                        is WeatherResult.Failure -> emit(LoadCurrentLocationWeatherResult.WeatherFailureResult(result.error))
                    }
                }
            }

            is CurrentLocationResult.Failure -> {
                emit(LoadCurrentLocationWeatherResult.LocationFailure(currentLocation.error))
            }
        }
    }
}
