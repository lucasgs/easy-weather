package com.dendron.easyweather.data.remote

import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.data.remote.model.toDomain
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteWeatherRepository @Inject constructor(private val api: WeatherApi) : WeatherRepository {

    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): Flow<Resource<Weather>> = flow {
        try {
            emit(Resource.Loading())
            val result = api.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
            ).toDomain()
            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }
}