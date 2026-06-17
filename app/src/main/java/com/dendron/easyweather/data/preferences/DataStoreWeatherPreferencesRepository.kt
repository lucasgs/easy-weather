package com.dendron.easyweather.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dendron.easyweather.domain.preferences.SavedLocationPreference
import com.dendron.easyweather.domain.preferences.SavedLocationSource
import com.dendron.easyweather.domain.preferences.TemperatureUnitPreference
import com.dendron.easyweather.domain.preferences.WeatherPreferences
import com.dendron.easyweather.domain.preferences.WeatherPreferencesRepository
import com.dendron.easyweather.domain.preferences.WindUnitPreference
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

private val Context.weatherPreferencesDataStore by preferencesDataStore(name = "weather_preferences")

@Singleton
class DataStoreWeatherPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : WeatherPreferencesRepository {

    override suspend fun getPreferences(): WeatherPreferences {
        val preferences = context.weatherPreferencesDataStore.data.first()
        return WeatherPreferences(
            temperatureUnit = TemperatureUnitPreference.fromStoredValue(preferences[Keys.temperatureUnit]),
            windUnit = WindUnitPreference.fromStoredValue(preferences[Keys.windUnit]),
            lastLocation = preferences.toSavedLocationPreference(),
        )
    }

    override suspend fun saveTemperatureUnit(unit: TemperatureUnitPreference) {
        context.weatherPreferencesDataStore.edit { preferences ->
            preferences[Keys.temperatureUnit] = unit.name
        }
    }

    override suspend fun saveWindUnit(unit: WindUnitPreference) {
        context.weatherPreferencesDataStore.edit { preferences ->
            preferences[Keys.windUnit] = unit.name
        }
    }

    override suspend fun saveLastLocation(location: SavedLocationPreference) {
        context.weatherPreferencesDataStore.edit { preferences ->
            preferences[Keys.lastLocationLatitude] = location.latitude
            preferences[Keys.lastLocationLongitude] = location.longitude
            location.name?.let { preferences[Keys.lastLocationName] = it }
                ?: preferences.remove(Keys.lastLocationName)
            preferences[Keys.lastLocationSource] = location.source.name
        }
    }

    private fun Preferences.toSavedLocationPreference(): SavedLocationPreference? {
        val latitude = this[Keys.lastLocationLatitude] ?: return null
        val longitude = this[Keys.lastLocationLongitude] ?: return null
        val source = SavedLocationSource.entries.firstOrNull {
            it.name == this[Keys.lastLocationSource]
        } ?: SavedLocationSource.Current
        return SavedLocationPreference(
            latitude = latitude,
            longitude = longitude,
            name = this[Keys.lastLocationName],
            source = source,
        )
    }

    private object Keys {
        val temperatureUnit = stringPreferencesKey("temperature_unit")
        val windUnit = stringPreferencesKey("wind_unit")
        val lastLocationLatitude = doublePreferencesKey("last_location_latitude")
        val lastLocationLongitude = doublePreferencesKey("last_location_longitude")
        val lastLocationName = stringPreferencesKey("last_location_name")
        val lastLocationSource = stringPreferencesKey("last_location_source")
    }
}
