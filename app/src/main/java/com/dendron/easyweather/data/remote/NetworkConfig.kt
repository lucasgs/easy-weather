package com.dendron.easyweather.data.remote

object NetworkConfig {
    const val WEATHER_BASE_URL = "https://api.open-meteo.com/v1/"
    const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com/v1/"

    const val CONNECT_TIMEOUT_SECONDS = 10L
    const val READ_TIMEOUT_SECONDS = 10L
    const val WRITE_TIMEOUT_SECONDS = 10L
    const val ENABLE_HTTP_LOGGING = false
}
