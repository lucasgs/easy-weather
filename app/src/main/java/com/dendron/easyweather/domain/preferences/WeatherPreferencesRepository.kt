package com.dendron.easyweather.domain.preferences

interface WeatherPreferencesRepository {
    suspend fun getPreferences(): WeatherPreferences
    suspend fun saveTemperatureUnit(unit: TemperatureUnitPreference)
    suspend fun saveWindUnit(unit: WindUnitPreference)
    suspend fun saveLastLocation(location: SavedLocationPreference)
}
