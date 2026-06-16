package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.WeatherResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RefreshWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(latitude: Double, longitude: Double): Flow<WeatherResult> =
        weatherRepository.getCurrentWeather(latitude, longitude)
}
