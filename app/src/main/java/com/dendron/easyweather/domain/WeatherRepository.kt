package com.dendron.easyweather.domain

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(latitude: Double, longitude: Double): Flow<WeatherResult>
}
