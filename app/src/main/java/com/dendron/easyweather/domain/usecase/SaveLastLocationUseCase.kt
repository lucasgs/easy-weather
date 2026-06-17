package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.preferences.SavedLocationPreference
import com.dendron.easyweather.domain.preferences.WeatherPreferencesRepository
import javax.inject.Inject

class SaveLastLocationUseCase @Inject constructor(
    private val weatherPreferencesRepository: WeatherPreferencesRepository,
) {
    suspend operator fun invoke(location: SavedLocationPreference) {
        weatherPreferencesRepository.saveLastLocation(location)
    }
}
