package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.domain.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadWeatherForCoordinatesUseCase @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val refreshWeatherUseCase: RefreshWeatherUseCase,
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        isRefresh: Boolean,
    ): Flow<Resource<Weather>> = if (isRefresh) {
        refreshWeatherUseCase(latitude, longitude)
    } else {
        getCurrentWeatherUseCase(latitude, longitude)
    }
}
