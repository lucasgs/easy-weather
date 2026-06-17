package com.dendron.easyweather.data.remote

import com.dendron.easyweather.data.local.WeatherCacheDao
import com.dendron.easyweather.data.local.model.toCachedEntity
import com.dendron.easyweather.data.local.model.toDomain as cachedToDomain
import com.dendron.easyweather.data.remote.model.toDomain
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteWeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val weatherCacheDao: WeatherCacheDao,
) : WeatherRepository {

    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): Flow<WeatherResult> = flow {
        emit(WeatherResult.Loading)

        val cachedWeather = runCatching {
            weatherCacheDao.getByCoordinates(latitude, longitude)
        }.getOrNull()

        if (cachedWeather != null) {
            emit(
                WeatherResult.Success(
                    weather = cachedWeather.cachedToDomain(),
                    lastUpdatedAtMillis = cachedWeather.cachedAtMillis,
                    isFromCache = true,
                ),
            )
        }

        try {
            val refreshedAtMillis = System.currentTimeMillis()
            val result = api.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
            ).toDomain()
            runCatching {
                weatherCacheDao.upsert(
                    result.toCachedEntity(
                        latitude = latitude,
                        longitude = longitude,
                        cachedAtMillis = refreshedAtMillis,
                    ),
                )
            }
            emit(
                WeatherResult.Success(
                    weather = result,
                    lastUpdatedAtMillis = refreshedAtMillis,
                ),
            )
        } catch (e: Exception) {
            if (cachedWeather != null) {
                emit(
                    WeatherResult.Success(
                        weather = cachedWeather.cachedToDomain(),
                        lastUpdatedAtMillis = cachedWeather.cachedAtMillis,
                        isStale = true,
                        isFromCache = true,
                    ),
                )
            } else {
                emit(WeatherResult.Failure(e.toWeatherFailure()))
            }
        }
    }
}
