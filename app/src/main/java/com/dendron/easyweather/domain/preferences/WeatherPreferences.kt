package com.dendron.easyweather.domain.preferences

data class WeatherPreferences(
    val temperatureUnit: TemperatureUnitPreference = TemperatureUnitPreference.Celsius,
    val windUnit: WindUnitPreference = WindUnitPreference.KilometersPerHour,
    val lastLocation: SavedLocationPreference? = null,
)

enum class TemperatureUnitPreference(val apiValue: String) {
    Celsius("celsius"),
    Fahrenheit("fahrenheit");

    companion object {
        fun fromStoredValue(value: String?): TemperatureUnitPreference = entries.firstOrNull {
            it.name == value
        } ?: Celsius

        fun fromDisplayUnit(value: String): TemperatureUnitPreference? = when (value) {
            "°C" -> Celsius
            "°F" -> Fahrenheit
            else -> null
        }
    }
}

enum class WindUnitPreference(val apiValue: String) {
    KilometersPerHour("kmh"),
    MilesPerHour("mph"),
    MetersPerSecond("ms"),
    Knots("kn");

    companion object {
        fun fromStoredValue(value: String?): WindUnitPreference = entries.firstOrNull {
            it.name == value
        } ?: KilometersPerHour

        fun fromDisplayUnit(value: String): WindUnitPreference? = when (value) {
            "km/h" -> KilometersPerHour
            "mph" -> MilesPerHour
            "m/s" -> MetersPerSecond
            "kn" -> Knots
            else -> null
        }
    }
}

data class SavedLocationPreference(
    val latitude: Double,
    val longitude: Double,
    val name: String?,
    val source: SavedLocationSource,
)

enum class SavedLocationSource {
    Current,
    Manual,
}
