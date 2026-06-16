package com.dendron.easyweather.data.remote

import com.dendron.easyweather.data.remote.model.toDomain
import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteWeatherRepository @Inject constructor(private val api: WeatherApi) : WeatherRepository {

    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): Flow<WeatherResult> = flow {
        emit(WeatherResult.Loading)
        try {
            val result = api.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
            ).toDomain()
            emit(WeatherResult.Success(result))
        } catch (e: HttpException) {
            emit(WeatherResult.Failure(WeatherFailure.Network))
        } catch (e: IOException) {
            emit(WeatherResult.Failure(WeatherFailure.Network))
        } catch (e: Exception) {
            emit(WeatherResult.Failure(WeatherFailure.Unknown))
        }
    }
}
