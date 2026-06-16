package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RefreshWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(latitude: Double, longitude: Double): Flow<Resource<Weather>> =
        weatherRepository.getCurrentWeather(latitude, longitude)
}
