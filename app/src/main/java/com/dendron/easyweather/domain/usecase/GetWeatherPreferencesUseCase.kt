package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.preferences.WeatherPreferences
import com.dendron.easyweather.domain.preferences.WeatherPreferencesRepository
import javax.inject.Inject

class GetWeatherPreferencesUseCase @Inject constructor(
    private val weatherPreferencesRepository: WeatherPreferencesRepository,
) {
    suspend operator fun invoke(): WeatherPreferences = weatherPreferencesRepository.getPreferences()
}
